
package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.CreditorProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class CreditorApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "return the list of creditors" in {
    val response = load("creditors/list.json")
    val cursorResponse = cursor[Creditor](response)
    val date = new DateTime()
    val map = Map(
      "after" -> "CU1",
      "before" -> "CU2",
      "limit" -> 50)
    val client = mock[HttpClient]
    (client.get _).expects("/creditors/", map).returning(Future { response.right })
    val result = Await.result(CreditorApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "get a single customer" in {
    val response = load("creditors/get.json")
    val wrappedResponse = wrap[Creditor](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/creditors/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(CreditorApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "create a customer" in {
    val request = load("creditors/create_request.json")
    val response = load("creditors/create_response.json")
    val wrappedRequest = wrap[CreditorCreateRequest](request)
    val wrappedResponse = wrap[Creditor](response)
    val client = mock[HttpClient]
    (client.post _).expects("/creditors/", request).returning(Future { response.right })
    val result = Await.result(CreditorApi(client).create(wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "update a customer" in {
    val request = load("creditors/update_request.json")
    val response = load("creditors/update_response.json")
    val wrappedRequest = wrap[CreditorUpdateRequest](request)
    val wrappedResponse = wrap[Creditor](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.put _).expects(s"/creditors/$id", request).returning(Future { response.right })
    val result = Await.result(CreditorApi(client).update(id, wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}

