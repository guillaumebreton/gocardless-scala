
package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.RoleProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class RoleApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "return the list of roles" in {
    val response = load("roles/list.json")
    val cursorResponse = cursor[Role](response)
    val date = new DateTime()
    val map = Map(
      "after" -> "BA122",
      "before" -> "BA123",
      "creditor" -> "CR1",
      "enabled" -> true,
      "limit" -> 50)
    val client = mock[HttpClient]
    (client.get _).expects("/roles/", map).returning(Future { response.right })
    val result = Await.result(RoleApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "get a single role" in {
    val response = load("roles/get.json")
    val wrappedResponse = wrap[Role](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/roles/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(RoleApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "create a role" in {
    val request = load("roles/create_request.json")
    val response = load("roles/create_response.json")
    val wrappedRequest = wrap[RoleCreateRequest](request)
    val wrappedResponse = wrap[Role](response)
    val client = mock[HttpClient]
    (client.post _).expects("/roles/", request).returning(Future { response.right })
    val result = Await.result(RoleApi(client).create(wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "update a role" in {
    val request = load("roles/update_request.json")
    val response = load("roles/update_response.json")
    val wrappedRequest = wrap[RoleUpdateRequest](request)
    val wrappedResponse = wrap[Role](response)
    val client = mock[HttpClient]
    (client.put _).expects(s"/roles/${wrappedResponse.entity.id}", request).returning(Future { response.right })
    val result = Await.result(RoleApi(client).update(wrappedResponse.entity.id, wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "disable a role" in {
    val response = load("roles/disable.json")
    val wrappedResponse = wrap[Role](response)
    val client = mock[HttpClient]
    (client.post _).expects(s"/roles/${wrappedResponse.entity.id}/actions/disable", "").returning(Future { response.right })
    val result = Await.result(RoleApi(client).disable(wrappedResponse.entity.id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}
