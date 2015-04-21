
package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.UserProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._
import DefaultJsonProtocol._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class UserApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "return the list of users" in {
    val response = load("users/list.json")
    val cursorResponse = cursor[User](response)
    val map = Map(
      "after" -> "BA122",
      "before" -> "BA123",
      "limit" -> 50)
    val client = mock[HttpClient]
    (client.get _).expects("/users/", map).returning(Future { response.right })
    val result = Await.result(UserApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "get a single user" in {
    val response = load("users/get.json")
    val wrappedResponse = wrap[User](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/users/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(UserApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "create a user" in {
    val request = load("users/create_request.json")
    val response = load("users/create_response.json")
    val wrappedRequest = wrap[UserCreateRequest](request)
    val wrappedResponse = wrap[User](response)
    val client = mock[HttpClient]
    (client.post _).expects("/users/", request).returning(Future { response.right })
    val result = Await.result(UserApi(client).create(wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "update a user" in {
    val request = load("users/update_request.json")
    val response = load("users/update_response.json")
    val wrappedRequest = wrap[UserUpdateRequest](request)
    val wrappedResponse = wrap[User](response)
    val client = mock[HttpClient]
    (client.put _).expects(s"/users/${wrappedResponse.entity.id}", request).returning(Future { response.right })
    val result = Await.result(UserApi(client).update(wrappedResponse.entity.id, wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "enable a user" in {
    val response = load("users/disable.json")
    val wrappedResponse = wrap[User](response)
    val client = mock[HttpClient]
    (client.post _).expects(s"/users/${wrappedResponse.entity.id}/actions/enable", "").returning(Future { response.right })
    val result = Await.result(UserApi(client).enable(wrappedResponse.entity.id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "disable a user" in {
    val response = load("users/enable.json")
    val wrappedResponse = wrap[User](response)
    val client = mock[HttpClient]
    (client.post _).expects(s"/users/${wrappedResponse.entity.id}/actions/disable", "").returning(Future { response.right })
    val result = Await.result(UserApi(client).disable(wrappedResponse.entity.id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}
