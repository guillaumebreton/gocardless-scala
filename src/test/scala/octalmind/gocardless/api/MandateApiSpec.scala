

package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.MandateProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._
import DefaultJsonProtocol._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class MandateApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "return the list of mandates" in {
    val response = load("mandates/list.json")
    val cursorResponse = cursor[Mandate](response)
    val date = new DateTime()
    val map = Map(
      "after" -> "BA122",
      "before" -> "BA123",
      "creditor" -> "CR1",
      "customer" -> "CU1234",
      "customer_bank_account" -> "123",
      "reference" -> 1234,
      "status" -> "pending_submission",
      "limit" -> 50)
    val client = mock[HttpClient]
    (client.get _).expects("/mandates/", map).returning(Future { response.right })
    val result = Await.result(MandateApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "get a single mandate pdf" in {
    val response = Array[Byte](1, 2, 3)
    val id = "MA123"
    val client = mock[HttpClient]
    (client.getPdf _).expects(getQuery(s"/mandates/$id"), "fr").returning(Future { response.right })
    val result = Await.result(MandateApi(client).getPdf(id, "fr"), 1.second)
    result must equal(response.right)
  }
  "get a single mandate" in {
    val response = load("mandates/get.json")
    val wrappedResponse = wrap[Mandate](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/mandates/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(MandateApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "create a mandate" in {
    val request = load("mandates/create_request.json")
    val response = load("mandates/create_response.json")
    val wrappedRequest = wrap[MandateCreateRequest](request)
    val wrappedResponse = wrap[Mandate](response)
    val client = mock[HttpClient]
    (client.post _).expects("/mandates/", request).returning(Future { response.right })
    val result = Await.result(MandateApi(client).create(wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "update a mandate" in {
    val request = load("mandates/update_request.json")
    val response = load("mandates/update_response.json")
    val wrappedRequest = wrap[MandateUpdateRequest](request)
    val wrappedResponse = wrap[Mandate](response)
    val client = mock[HttpClient]
    (client.put _).expects(s"/mandates/${wrappedResponse.entity.id}", request).returning(Future { response.right })
    val result = Await.result(MandateApi(client).update(wrappedResponse.entity.id, wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "cancel a mandate" in {
    val request = load("mandates/cancel_request.json")
    val response = load("mandates/cancel_response.json")
    val wrappedRequest = request.parseJson.convertTo[MandateCancelRequest]
    val wrappedResponse = wrap[Mandate](response)
    val client = mock[HttpClient]
    (client.post _).expects(s"/mandates/${wrappedResponse.entity.id}/actions/cancel", request).returning(Future { response.right })
    val result = Await.result(MandateApi(client).cancel(wrappedResponse.entity.id, wrappedRequest), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "reinstate a mandate" in {
    val request = load("mandates/reinstate_request.json")
    val response = load("mandates/reinstate_response.json")
    val wrappedRequest = request.parseJson.convertTo[MandateReinstateRequest]
    val wrappedResponse = wrap[Mandate](response)
    val client = mock[HttpClient]
    (client.post _).expects(s"/mandates/${wrappedResponse.entity.id}/actions/reinstate", request).returning(Future { response.right })
    val result = Await.result(MandateApi(client).reinstate(wrappedResponse.entity.id, wrappedRequest), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}
