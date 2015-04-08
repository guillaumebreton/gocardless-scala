package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import octalmind.gocardless.FakeHttpClient

import octalmind.gocardless.model.ApiKeyProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._
import spray.http.HttpMethods

class KeyApiSpec extends ApiSpec {

  "return the list of keys" in {

    val response = cursor[ApiKey]("keys/list_keys.json")
    val client = FakeHttpClient(response.toJson)
    val api = KeyApi(client)

    val map = Map(
      "after" -> "CU1",
      "before" -> "CU2",
      "enabled" -> true,
      "limit" -> 50,
      "role" -> "RO123")
    val result = Await.result(api.query(map), 1.second)
    client.called must equal(true)
    client.uri must equal(getQuery("/api_keys/", map))
    result must equal(response)

  }
  "get a single key" in {
    val response = wrap[ApiKey]("keys/get_key.json")
    val client = FakeHttpClient(response.toJson)
    val id = response.entity.id
    val result = Await.result(KeyApi(client).retrieve(id), 1.second)
    client.called must equal(true)
    client.uri must equal(getQuery("/api_keys/" + id))
    result must equal(response.entity)

  }
  "create a key" in {

    val request = wrap[ApiKeyCreateRequest]("keys/create_key_request.json")
    val response = wrap[ApiKey]("keys/create_key_response.json")

    val client = FakeHttpClient(response.toJson)
    val result = Await.result(KeyApi(client).create(request.entity), 1.second)

    client.called must equal(true)
    client.entity[ApiKeyCreateRequest] must equal(request)
    client.uri must equal(getQuery("/api_keys/"))
    client.method must equal(HttpMethods.POST)
    result must equal(response.entity)

  }
  "update a key" in {
    val request = wrap[ApiKeyUpdateRequest]("keys/update_key_request.json")
    val response = wrap[ApiKey]("keys/update_key_response.json")

    val client = FakeHttpClient(response.toJson)
    val id = response.entity.id
    val result = Await.result(KeyApi(client).update(id, request.entity), 1.second)
    client.called must equal(true)
    client.entity[ApiKeyUpdateRequest] must equal(request)
    client.uri must equal(getQuery("/api_keys/" + id))
    client.method must equal(HttpMethods.PUT)
    result must equal(response.entity)
  }

  "disable a key" in {
    val response = wrap[ApiKey]("keys/disable_key.json")
    val id = response.entity.id
    val client = FakeHttpClient(response.toJson)
    val result = Await.result(KeyApi(client).disable(id), 1.second)

    client.called must equal(true)
    client.uri must equal(getQuery(s"/api_keys/$id/actions/disable"))
    client.method must equal(HttpMethods.POST)
    result must equal(response.entity)
  }

}

