
package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.PayoutProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._
import DefaultJsonProtocol._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class PayoutApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "return the list of payouts" in {
    val response = load("payout/list.json")
    val cursorResponse = cursor[Payout](response)
    val map = Map(
      "after" -> "BA122",
      "before" -> "BA123",
      "limit" -> 50)
    val client = mock[HttpClient]
    (client.get _).expects("/payouts/", map).returning(Future { response.right })
    val result = Await.result(PayoutApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "get a single payout" in {
    val response = load("payout/get.json")
    val wrappedResponse = wrap[Payout](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/payouts/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(PayoutApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}
