
package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.PaymentProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._
import DefaultJsonProtocol._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class PaymentApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "return the list of payments" in {
    val response = load("payment/list.json")
    val cursorResponse = cursor[Payment](response)
    val date = new DateTime()
    val map = Map(
      "after" -> "BA122",
      "before" -> "BA123",
      "creditor" -> "CR1",
      "customer" -> "CU1234",
      "customer_bank_account" -> "123",
      "mandate" -> 1234,
      "status" -> "pending_submission",
      "subscription" -> "SB",
      "created_at_gt" -> date,
      "created_at_gte" -> date,
      "created_at_lt" -> date,
      "created_at_lte" -> date,
      "limit" -> 50)
    val client = mock[HttpClient]
    (client.get _).expects("/payments/", map).returning(Future { response.right })
    val result = Await.result(PaymentApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "get a single payment" in {
    val response = load("payment/get.json")
    val wrappedResponse = wrap[Payment](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/payments/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(PaymentApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "create a payment" in {
    val request = load("payment/create_request.json")
    val response = load("payment/create_response.json")
    val wrappedRequest = wrap[PaymentCreateRequest](request)
    val wrappedResponse = wrap[Payment](response)
    val client = mock[HttpClient]
    (client.post _).expects("/payments/", request).returning(Future { response.right })
    val result = Await.result(PaymentApi(client).create(wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "update a payment" in {
    val request = load("payment/update_request.json")
    val response = load("payment/update_response.json")
    val wrappedRequest = wrap[PaymentUpdateRequest](request)
    val wrappedResponse = wrap[Payment](response)
    val client = mock[HttpClient]
    (client.put _).expects(s"/payments/${wrappedResponse.entity.id}", request).returning(Future { response.right })
    val result = Await.result(PaymentApi(client).update(wrappedResponse.entity.id, wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "cancel a payment" in {
    val request = load("payment/cancel_request.json")
    val response = load("payment/cancel_response.json")
    val wrappedRequest = request.parseJson.convertTo[PaymentCancelRequest]
    val wrappedResponse = wrap[Payment](response)
    val client = mock[HttpClient]
    (client.post _).expects(s"/payments/${wrappedResponse.entity.id}/actions/cancel", request).returning(Future { response.right })
    val result = Await.result(PaymentApi(client).cancel(wrappedResponse.entity.id, wrappedRequest), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "retry a payment" in {
    val request = load("payment/retry_request.json")
    val response = load("payment/retry_response.json")
    val wrappedRequest = request.parseJson.convertTo[PaymentRetryRequest]
    val wrappedResponse = wrap[Payment](response)
    val client = mock[HttpClient]
    (client.post _).expects(s"/payments/${wrappedResponse.entity.id}/actions/retry", request).returning(Future { response.right })
    val result = Await.result(PaymentApi(client).retry(wrappedResponse.entity.id, wrappedRequest), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}
