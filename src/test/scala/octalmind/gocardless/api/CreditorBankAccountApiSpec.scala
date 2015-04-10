package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import octalmind.gocardless.FakeHttpClient

import octalmind.gocardless.model.CreditorBankAccountProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._
import spray.http.HttpMethods

class CreditorBankAccountSpec extends ApiSpec {

  "return the list of bank accounts" in {

    val response = cursor[CreditorBankAccount]("creditor_ba/list_creditor_ba.json")
    val client = FakeHttpClient(response.toJson)
    val api = CreditorBankAccountApi(client)

    val map = Map(
      "after" -> "CU1",
      "before" -> "CU2",
      "enabled" -> true,
      "limit" -> 50,
      "role" -> "RO123")
    val result = Await.result(api.query(map), 1.second)
    client.called must equal(true)
    client.uri must equal(getQuery("/creditor_bank_accounts/", map))
    result must equal(response)

  }
  "get a single bank account" in {
    val response = wrap[CreditorBankAccount]("creditor_ba/get_creditor_ba.json")
    val client = FakeHttpClient(response.toJson)
    val id = response.entity.id
    val result = Await.result(CreditorBankAccountApi(client).retrieve(id), 1.second)
    client.called must equal(true)
    client.uri must equal(getQuery("/creditor_bank_accounts/" + id))
    result must equal(response.entity)

  }
  "create a bank account" in {

    val request = wrap[CreditorBankAccountCreateRequest]("creditor_ba/create_creditor_ba_request.json")
    val response = wrap[CreditorBankAccount]("creditor_ba/create_creditor_ba_response.json")

    val client = FakeHttpClient(response.toJson)
    val result = Await.result(CreditorBankAccountApi(client).create(request.entity), 1.second)

    client.called must equal(true)
    client.entity[CreditorBankAccountCreateRequest] must equal(request)
    client.uri must equal(getQuery("/creditor_bank_accounts/"))
    client.method must equal(HttpMethods.POST)
    result must equal(response.entity)
  }

  "disable a bank account" in {
    val response = wrap[CreditorBankAccount]("creditor_ba/disable_creditor_ba.json")
    val id = response.entity.id
    val client = FakeHttpClient(response.toJson)
    val result = Await.result(CreditorBankAccountApi(client).disable(id), 1.second)

    client.called must equal(true)
    client.uri must equal(getQuery(s"/creditor_bank_accounts/$id/actions/disable"))
    client.method must equal(HttpMethods.POST)
    result must equal(response.entity)
  }

}

