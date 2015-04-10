
package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import octalmind.gocardless.FakeHttpClient

import octalmind.gocardless.model.CustomerBankAccountProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._
import spray.http.HttpMethods

class CustomerBankAccountSpec extends ApiSpec {

  "return the list of bank accounts" in {

    val response = cursor[CustomerBankAccount]("customer_ba/list_customer_ba.json")
    val client = FakeHttpClient(response.toJson)
    val api = CustomerBankAccountApi(client)

    val map = Map(
      "after" -> "CU1",
      "before" -> "CU2",
      "enabled" -> true,
      "limit" -> 50,
      "customer" -> "CU123")
    val result = Await.result(api.query(map), 1.second)
    client.called must equal(true)
    client.uri must equal(getQuery("/customer_bank_accounts/", map))
    result must equal(response)

  }
  "get a single bank account" in {
    val response = wrap[CustomerBankAccount]("customer_ba/get_customer_ba.json")
    val client = FakeHttpClient(response.toJson)
    val id = response.entity.id
    val result = Await.result(CustomerBankAccountApi(client).retrieve(id), 1.second)
    client.called must equal(true)
    client.uri must equal(getQuery("/customer_bank_accounts/" + id))
    result must equal(response.entity)

  }
  "create a bank account" in {

    val request = wrap[CustomerBankAccountCreateRequest]("customer_ba/create_customer_ba_request.json")
    val response = wrap[CustomerBankAccount]("customer_ba/create_customer_ba_response.json")

    val client = FakeHttpClient(response.toJson)
    val result = Await.result(CustomerBankAccountApi(client).create(request.entity), 1.second)

    client.called must equal(true)
    client.entity[CustomerBankAccountCreateRequest] must equal(request)
    client.uri must equal(getQuery("/customer_bank_accounts/"))
    client.method must equal(HttpMethods.POST)
    result must equal(response.entity)
  }

  "update a bank account" in {

    val request = wrap[CustomerBankAccountUpdateRequest]("customer_ba/update_customer_ba_request.json")
    val response = wrap[CustomerBankAccount]("customer_ba/update_customer_ba_response.json")

    val client = FakeHttpClient(response.toJson)
    val result = Await.result(CustomerBankAccountApi(client).update(response.entity.id, request.entity), 1.second)

    client.called must equal(true)
    client.entity[CustomerBankAccountUpdateRequest] must equal(request)
    client.uri must equal(getQuery(s"/customer_bank_accounts/${response.entity.id}"))
    client.method must equal(HttpMethods.PUT)
    result must equal(response.entity)
  }
  "disable a bank account" in {
    val response = wrap[CustomerBankAccount]("customer_ba/disable_customer_ba.json")
    val id = response.entity.id
    val client = FakeHttpClient(response.toJson)
    val result = Await.result(CustomerBankAccountApi(client).disable(id), 1.second)

    client.called must equal(true)
    client.uri must equal(getQuery(s"/customer_bank_accounts/$id/actions/disable"))
    client.method must equal(HttpMethods.POST)
    result must equal(response.entity)
  }

}

