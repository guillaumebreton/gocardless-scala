package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import octalmind.gocardless.FakeHttpClient
import org.joda.time._

import octalmind.gocardless.model.CustomerProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._
import spray.http.HttpMethods

class CustomerApiSpec extends ApiSpec {

  "return the list of customers" in {

    val response = cursor[Customer]("customers/list_customers.json")
    val client = FakeHttpClient(response.toJson)
    val api = CustomerApi(client)

    val date = new DateTime()
    val map = Map(
      "after" -> "CU1",
      "before" -> "CU2",
      "created_at_gt" -> date,
      "created_at_gte" -> date,
      "created_at_lt" -> date,
      "created_at_lte" -> date)
    val result = Await.result(api.query(map), 1.second)
    client.called must equal(true)
    client.uri must equal(getQuery("/customers/", map))
    result must equal(response)

  }
  "get a single customer" in {
    val response = wrap[Customer]("customers/get_customer.json")
    val client = FakeHttpClient(response.toJson)
    val id = response.entity.id
    val result = Await.result(CustomerApi(client).retrieve(id), 1.second)
    client.called must equal(true)
    client.uri must equal(getQuery("/customers/" + id))
    result must equal(response.entity)

  }
  "create a customer" in {

    val request = wrap[CustomerRequest]("customers/create_customer_request.json")
    val response = wrap[Customer]("customers/create_customer_response.json")

    val client = FakeHttpClient(response.toJson)
    val result = Await.result(CustomerApi(client).create(request.entity), 1.second)

    client.called must equal(true)
    client.entity[CustomerRequest] must equal(request)
    client.uri must equal(getQuery("/customers/"))
    client.method must equal(HttpMethods.POST)
    result must equal(response.entity)

  }
  "update a customer" in {
    val request = wrap[CustomerRequest]("customers/update_customer_request.json")
    val response = wrap[Customer]("customers/update_customer_response.json")

    val client = FakeHttpClient(response.toJson)
    val id = response.entity.id
    val result = Await.result(CustomerApi(client).update(id, request.entity), 1.second)
    client.called must equal(true)
    client.entity[CustomerRequest] must equal(request)
    client.uri must equal(getQuery("/customers/" + id))
    client.method must equal(HttpMethods.PUT)
    result must equal(response.entity)
  }
}

