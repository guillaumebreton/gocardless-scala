
package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.RedirectFlowProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._
import DefaultJsonProtocol._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class RedirectFlowApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "create a redirect flow" in {
    val request = load("redirect_flow/create_request.json")
    val response = load("redirect_flow/create_response.json")
    val wrappedRequest = wrap[RedirectFlowCreateRequest](request)
    val wrappedResponse = wrap[RedirectFlow](response)
    val client = mock[HttpClient]
    (client.post _).expects("/redirect_flows/", request).returning(Future { response.right })
    val result = Await.result(RedirectFlowApi(client).create(wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "get a single redirect flow" in {
    val response = load("redirect_flow/get.json")
    val wrappedResponse = wrap[RedirectFlow](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/redirect_flows/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(RedirectFlowApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "complete a redirect flow" in {
    val request = load("redirect_flow/complete_request.json")
    val response = load("redirect_flow/complete_response.json")
    val wrappedRequest = request.parseJson.convertTo[RedirectFlowCompleteRequest]
    val wrappedResponse = wrap[RedirectFlow](response)
    val client = mock[HttpClient]
    (client.post _).expects(s"/redirect_flows/${wrappedResponse.entity.id}/actions/complete", request).returning(Future { response.right })
    val result = Await.result(RedirectFlowApi(client).complete(wrappedResponse.entity.id, wrappedRequest), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}
