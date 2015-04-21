
package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.SubscriptionProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class SubscriptionApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "return the list of subscriptions" in {
    val response = load("subscriptions/list.json")
    val cursorResponse = cursor[Subscription](response)
    val date = new DateTime()
    val map = Map(
      "after" -> "BA122",
      "before" -> "BA123",
      "limit" -> 50)
    val client = mock[HttpClient]
    (client.get _).expects("/subscriptions/", map).returning(Future { response.right })
    val result = Await.result(SubscriptionApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "get a single subscription" in {
    val response = load("subscriptions/get.json")
    val wrappedResponse = wrap[Subscription](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/subscriptions/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(SubscriptionApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "create a subscription" in {
    val request = load("subscriptions/create_request.json")
    val response = load("subscriptions/create_response.json")
    val wrappedRequest = wrap[SubscriptionCreateRequest](request)
    val wrappedResponse = wrap[Subscription](response)
    val client = mock[HttpClient]
    (client.post _).expects("/subscriptions/", request).returning(Future { response.right })
    val result = Await.result(SubscriptionApi(client).create(wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "update a subscription" in {
    val request = load("subscriptions/update_request.json")
    val response = load("subscriptions/update_response.json")
    val wrappedRequest = wrap[SubscriptionUpdateRequest](request)
    val wrappedResponse = wrap[Subscription](response)
    val client = mock[HttpClient]
    (client.put _).expects(s"/subscriptions/${wrappedResponse.entity.id}", request).returning(Future { response.right })
    val result = Await.result(SubscriptionApi(client).update(wrappedResponse.entity.id, wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "cancel a subscription" in {
    val request = load("subscriptions/cancel_request.json")
    val response = load("subscriptions/cancel_response.json")
    val wrappedRequest = request.parseJson.convertTo[SubscriptionCancelRequest]
    val wrappedResponse = wrap[Subscription](response)
    val client = mock[HttpClient]
    (client.post _).expects(s"/subscriptions/${wrappedResponse.entity.id}/actions/cancel", request).returning(Future { response.right })
    val result = Await.result(SubscriptionApi(client).cancel(wrappedResponse.entity.id, wrappedRequest), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}
