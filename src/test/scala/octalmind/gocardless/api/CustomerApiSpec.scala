package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.CustomerProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class CustomerApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "return the list of customers" in {
    val response = load("customers/list_customers.json")
    val cursorResponse = cursor[Customer](response)
    val date = new DateTime()
    val map = Map(
      "after" -> "CU1",
      "before" -> "CU2",
      "created_at_gt" -> date,
      "created_at_gte" -> date,
      "created_at_lt" -> date,
      "created_at_lte" -> date)
    val client = mock[HttpClient]
    (client.get _).expects("/customers/", map).returning(Future { response.right })
    val result = Await.result(CustomerApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "get a single customer" in {
    val response = load("customers/get_customer.json")
    val wrappedResponse = wrap[Customer](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/customers/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(CustomerApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "create a customer" in {
    val request = load("customers/create_customer_request.json")
    val response = load("customers/create_customer_response.json")
    val wrappedRequest = wrap[CustomerRequest](request)
    val wrappedResponse = wrap[Customer](response)
    val client = mock[HttpClient]
    (client.post _).expects("/customers/", request).returning(Future { response.right })
    val result = Await.result(CustomerApi(client).create(wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "update a customer" in {
    val request = load("customers/update_customer_request.json")
    val response = load("customers/update_customer_response.json")
    val wrappedRequest = wrap[CustomerRequest](request)
    val wrappedResponse = wrap[Customer](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.put _).expects(s"/customers/$id", request).returning(Future { response.right })
    val result = Await.result(CustomerApi(client).update(id, wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}

