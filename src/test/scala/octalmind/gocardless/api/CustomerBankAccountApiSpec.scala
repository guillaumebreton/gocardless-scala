
package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.CustomerBankAccountProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class CustomerBankAccountApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "return the list of customer bank accounts" in {
    val response = load("customer_bas/list.json")
    val cursorResponse = cursor[CustomerBankAccount](response)
    val date = new DateTime()
    val map = Map(
      "after" -> "BA122",
      "before" -> "BA123",
      "creditor" -> "CR1",
      "enabled" -> true,
      "limit" -> 50)
    val client = mock[HttpClient]
    (client.get _).expects("/customer_bank_accounts/", map).returning(Future { response.right })
    val result = Await.result(CustomerBankAccountApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "get a single customer bank account" in {
    val response = load("customer_bas/get.json")
    val wrappedResponse = wrap[CustomerBankAccount](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/customer_bank_accounts/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(CustomerBankAccountApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "create a customer bank account" in {
    val request = load("customer_bas/create_request.json")
    val response = load("customer_bas/create_response.json")
    val wrappedRequest = wrap[CustomerBankAccountCreateRequest](request)
    val wrappedResponse = wrap[CustomerBankAccount](response)
    val client = mock[HttpClient]
    (client.post _).expects("/customer_bank_accounts/", request).returning(Future { response.right })
    val result = Await.result(CustomerBankAccountApi(client).create(wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "update a customer bank account" in {
    val request = load("customer_bas/update_request.json")
    val response = load("customer_bas/update_response.json")
    val wrappedRequest = wrap[CustomerBankAccountUpdateRequest](request)
    val wrappedResponse = wrap[CustomerBankAccount](response)
    val client = mock[HttpClient]
    (client.put _).expects(s"/customer_bank_accounts/${wrappedResponse.entity.id}", request).returning(Future { response.right })
    val result = Await.result(CustomerBankAccountApi(client).update(wrappedResponse.entity.id, wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "disable a customer bank account" in {
    val response = load("customer_bas/disable.json")
    val wrappedResponse = wrap[CustomerBankAccount](response)
    val client = mock[HttpClient]
    (client.post _).expects(s"/customer_bank_accounts/${wrappedResponse.entity.id}/actions/disable", "").returning(Future { response.right })
    val result = Await.result(CustomerBankAccountApi(client).disable(wrappedResponse.entity.id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}
