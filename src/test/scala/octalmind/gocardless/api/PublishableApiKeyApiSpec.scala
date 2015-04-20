
package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.PublishableApiKeyProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class PublishableApiKeyApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "return the list of publish api keys" in {
    val response = load("publishable_keys/list.json")
    val cursorResponse = cursor[PublishableApiKey](response)
    val date = new DateTime()
    val map = Map(
      "after" -> "BA122",
      "before" -> "BA123",
      "creditor" -> "CR1",
      "enabled" -> true,
      "limit" -> 50)
    val client = mock[HttpClient]
    (client.get _).expects("/publishable_api_keys/", map).returning(Future { response.right })
    val result = Await.result(PublishableApiKeyApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "get a single publish api key" in {
    val response = load("publishable_keys/get.json")
    val wrappedResponse = wrap[PublishableApiKey](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/publishable_api_keys/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(PublishableApiKeyApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "create a publish api key" in {
    val request = load("publishable_keys/create_request.json")
    val response = load("publishable_keys/create_response.json")
    val wrappedRequest = wrap[PublishableApiKeyRequest](request)
    val wrappedResponse = wrap[PublishableApiKey](response)
    val client = mock[HttpClient]
    (client.post _).expects("/publishable_api_keys/", request).returning(Future { response.right })
    val result = Await.result(PublishableApiKeyApi(client).create(wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "update a publish api key" in {
    val request = load("publishable_keys/update_request.json")
    val response = load("publishable_keys/update_response.json")
    val wrappedRequest = wrap[PublishableApiKeyRequest](request)
    val wrappedResponse = wrap[PublishableApiKey](response)
    val client = mock[HttpClient]
    (client.put _).expects(s"/publishable_api_keys/${wrappedResponse.entity.id}", request).returning(Future { response.right })
    val result = Await.result(PublishableApiKeyApi(client).update(wrappedResponse.entity.id, wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "disable a publish api key" in {
    val response = load("publishable_keys/disable.json")
    val wrappedResponse = wrap[PublishableApiKey](response)
    val client = mock[HttpClient]
    (client.post _).expects(s"/publishable_api_keys/${wrappedResponse.entity.id}/actions/disable", "").returning(Future { response.right })
    val result = Await.result(PublishableApiKeyApi(client).disable(wrappedResponse.entity.id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}
