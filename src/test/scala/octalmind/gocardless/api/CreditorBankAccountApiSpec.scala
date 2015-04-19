package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.CreditorBankAccountProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class CreditorBankAccountApiSpec extends ApiSpec {

  val emptyQuery = Map[String, Any]()

  "return the list of creditor bank accounts" in {
    val response = load("creditor_ba/list.json")
    val cursorResponse = cursor[CreditorBankAccount](response)
    val date = new DateTime()
    val map = Map(
      "after" -> "BA122",
      "before" -> "BA123",
      "creditor" -> "CR1",
      "enabled" -> true,
      "limit" -> 50)
    val client = mock[HttpClient]
    (client.get _).expects("/creditor_bank_accounts/", map).returning(Future { response.right })
    val result = Await.result(CreditorBankAccountApi(client).list(map), 1.second)
    result must equal(cursorResponse.right)
  }
  "get a single creditor bank account" in {
    val response = load("creditor_ba/get.json")
    val wrappedResponse = wrap[CreditorBankAccount](response)
    val id = wrappedResponse.entity.id
    val client = mock[HttpClient]
    (client.get _).expects(getQuery(s"/creditor_bank_accounts/$id"), emptyQuery).returning(Future { response.right })
    val result = Await.result(CreditorBankAccountApi(client).get(id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "create a creditor bank account" in {
    val request = load("creditor_ba/create_request.json")
    val response = load("creditor_ba/create_response.json")
    val wrappedRequest = wrap[CreditorBankAccountCreateRequest](request)
    val wrappedResponse = wrap[CreditorBankAccount](response)
    val client = mock[HttpClient]
    (client.post _).expects("/creditor_bank_accounts/", request).returning(Future { response.right })
    val result = Await.result(CreditorBankAccountApi(client).create(wrappedRequest.entity), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
  "disable a creditor bank account" in {
    val response = load("creditor_ba/disable.json")
    val wrappedResponse = wrap[CreditorBankAccount](response)
    val client = mock[HttpClient]
    (client.post _).expects(s"/creditor_bank_accounts/${wrappedResponse.entity.id}/actions/disable", "").returning(Future { response.right })
    val result = Await.result(CreditorBankAccountApi(client).disable(wrappedResponse.entity.id), 1.second)
    result must equal(wrappedResponse.entity.right)
  }
}
