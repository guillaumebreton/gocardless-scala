
package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import octalmind.gocardless.FakeHttpClient
import org.joda.time._

import octalmind.gocardless.model.CreditorProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._
import spray.http.HttpMethods

class CreditorApiSpec extends ApiSpec {

  "return the list of creditors" in {

    val response = cursor[Creditor]("creditors/list_creditors.json")
    val client = FakeHttpClient(response.toJson)
    val api = CreditorApi(client)

    val date = new DateTime()
    val map = Map(
      "after" -> "CR1",
      "before" -> "CR2")
    val result = Await.result(api.query(map), 1.second)
    client.called must equal(true)
    client.uri must equal(getQuery("/creditors/", map))
    result must equal(response)

  }
  "get a single creditor" in {
    val response = wrap[Creditor]("creditors/get_creditor.json")
    val client = FakeHttpClient(response.toJson)
    val id = response.entity.id
    val result = Await.result(CreditorApi(client).retrieve(id), 1.second)
    client.called must equal(true)
    client.uri must equal(getQuery("/creditors/" + id))
    result must equal(response.entity)

  }
  "create a creditor" in {

    val request = wrap[CreditorCreateRequest]("creditors/create_creditor_request.json")
    val response = wrap[Creditor]("creditors/create_creditor_response.json")

    val client = FakeHttpClient(response.toJson)
    val result = Await.result(CreditorApi(client).create(request.entity), 1.second)

    client.called must equal(true)
    client.entity[CreditorCreateRequest] must equal(request)
    client.uri must equal(getQuery("/creditors/"))
    client.method must equal(HttpMethods.POST)
    result must equal(response.entity)

  }
  "update a creditor" in {
    val request = wrap[CreditorUpdateRequest]("creditors/update_creditor_request.json")
    val response = wrap[Creditor]("creditors/update_creditor_response.json")

    val client = FakeHttpClient(response.toJson)
    val id = response.entity.id
    val result = Await.result(CreditorApi(client).update(id, request.entity), 1.second)
    client.called must equal(true)
    client.entity[CreditorUpdateRequest] must equal(request)
    client.uri must equal(getQuery("/creditors/" + id))
    client.method must equal(HttpMethods.PUT)
    result must equal(response.entity)
  }
}

