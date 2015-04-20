

package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.ApiKeyProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class ApiKeyApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "return the list of keys" in {
    val response = load("keys/list.json")
    val cursorResponse = cursor[ApiKey](response)
    val date = new DateTime()
    val map = Map(
      "after" -> "BA122",
      "before" -> "BA123",
      "limit" -> 50)
    val client = mock[HttpClient]
    (client.get _).expects("/api_keys/", map).returning(Future { response.right })
    val result = Await.result(ApiKeyApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "get a single key" in {
    val response = load("keys/get.json")
    val wrappedResponse = wrap[ApiKey](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/api_keys/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(ApiKeyApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "create a key" in {
    val request = load("keys/create_request.json")
    val response = load("keys/create_response.json")
    val wrappedRequest = wrap[ApiKeyCreateRequest](request)
    val wrappedResponse = wrap[ApiKey](response)
    val client = mock[HttpClient]
    (client.post _).expects("/api_keys/", request).returning(Future { response.right })
    val result = Await.result(ApiKeyApi(client).create(wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "update a key" in {
    val request = load("keys/update_request.json")
    val response = load("keys/update_response.json")
    val wrappedRequest = wrap[ApiKeyUpdateRequest](request)
    val wrappedResponse = wrap[ApiKey](response)
    val client = mock[HttpClient]
    (client.put _).expects(s"/api_keys/${wrappedResponse.entity.id}", request).returning(Future { response.right })
    val result = Await.result(ApiKeyApi(client).update(wrappedResponse.entity.id, wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "disable a key" in {
    val response = load("keys/disable.json")
    val wrappedResponse = wrap[ApiKey](response)
    val client = mock[HttpClient]
    (client.post _).expects(s"/api_keys/${wrappedResponse.entity.id}/actions/disable", "").returning(Future { response.right })
    val result = Await.result(ApiKeyApi(client).disable(wrappedResponse.entity.id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}
