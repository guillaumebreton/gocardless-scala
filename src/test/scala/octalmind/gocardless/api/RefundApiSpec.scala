package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.RefundProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class RefundApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "return the list of refunds" in {
    val response = load("refunds/list.json")
    val cursorResponse = cursor[Refund](response)
    val date = new DateTime()
    val map = Map(
      "after" -> "CU1",
      "before" -> "CU2",
      "created_at_gt" -> date,
      "created_at_gte" -> date,
      "created_at_lt" -> date,
      "created_at_lte" -> date)
    val client = mock[HttpClient]
    (client.get _).expects("/refunds/", map).returning(Future { response.right })
    val result = Await.result(RefundApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "get a single refund" in {
    val response = load("refunds/get.json")
    val wrappedResponse = wrap[Refund](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/refunds/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(RefundApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "create a refund" in {
    val request = load("refunds/create_request.json")
    val response = load("refunds/create_response.json")
    val wrappedRequest = wrap[RefundCreateRequest](request)
    val wrappedResponse = wrap[Refund](response)
    val client = mock[HttpClient]
    (client.post _).expects("/refunds/", request).returning(Future { response.right })
    val result = Await.result(RefundApi(client).create(wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "update a refund" in {
    val request = load("refunds/update_request.json")
    val response = load("refunds/update_response.json")
    val wrappedRequest = wrap[RefundUpdateRequest](request)
    val wrappedResponse = wrap[Refund](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.put _).expects(s"/refunds/$id", request).returning(Future { response.right })
    val result = Await.result(RefundApi(client).update(id, wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}

