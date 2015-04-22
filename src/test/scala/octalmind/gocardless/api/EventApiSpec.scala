
package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.EventProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._
import DefaultJsonProtocol._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class EventApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "return the list of events" in {
    val response = load("events/list.json")
    val cursorResponse = cursor[Event](response)
    val map = Map(
      "after" -> "EV122",
      "before" -> "EV123",
      "limit" -> 50)
    val client = mock[HttpClient]
    (client.get _).expects("/events/", map).returning(Future { response.right })
    val result = Await.result(EventApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "return the list of events with criteria" in {
    val response = load("events/list_with_filters.json")
    val cursorResponse = cursor[Event](response)
    val map = Map(
      "after" -> "EV122",
      "before" -> "EV123",
      "limit" -> 50)
    val client = mock[HttpClient]
    (client.get _).expects("/events/", map).returning(Future { response.right })
    val result = Await.result(EventApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "get a single event" in {
    val response = load("events/get.json")
    val wrappedResponse = wrap[Event](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/events/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(EventApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}
