package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import octalmind.gocardless.FakeHttpClient
import org.joda.time._

import octalmind.gocardless.model.CustomerProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._
import spray.http.HttpMethods

class CustomerApiSpec extends WordSpec with MustMatchers {

  "return the list of customers" in {

    val response = cursor[Customer]("list_customers.json")
    val client = FakeHttpClient(response.toJson)
    val api = CustomerApi(client)

    val date = new DateTime()
    val map = Map(
      "after" -> "CU1",
      "before" -> "CU2",
      "created_at_gt" -> date,
      "created_at_gte" -> date,
      "created_at_lt" -> date,
      "created_at_lte" -> date)
    val result = Await.result(api.query(map), 1.second)
    client.called must equal(true)
    client.uri must equal(getQuery("/customers/", map))
    result must equal(response)

  }
  "get a single customer" in {
    val response = wrap[Customer]("get_customer.json")
    val client = FakeHttpClient(response.toJson)
    val id = response.entity.id.get
    val result = Await.result(CustomerApi(client).retrieve(id), 1.second)
    client.called must equal(true)
    client.uri must equal(getQuery("/customers/" + id))
    result must equal(response.entity)

  }
  "create a customer" in {

    val request = wrap[CustomerRequest]("create_customer_request.json")
    val response = wrap[Customer]("create_customer_response.json")

    val client = FakeHttpClient(response.toJson)
    val result = Await.result(CustomerApi(client).create(request.entity), 1.second)

    client.called must equal(true)
    client.entity[CustomerRequest] must equal(request)
    client.uri must equal(getQuery("/customers/"))
    client.method must equal(HttpMethods.POST)
    result must equal(response.entity)

  }
  "update a customer" in {
    val request = wrap[CustomerRequest]("update_customer_request.json")
    val response = wrap[Customer]("update_customer_response.json")

    val client = FakeHttpClient(response.toJson)
    val id = response.entity.id.get
    val result = Await.result(CustomerApi(client).update(id, request.entity), 1.second)
    client.called must equal(true)
    client.entity[CustomerRequest] must equal(request)
    client.uri must equal(getQuery("/customers/" + id))
    client.method must equal(HttpMethods.PUT)
    result must equal(response.entity)
  }

  private[this] def cursor[T: JsonFormat: reflect.ClassTag](filename: String): Cursor[T] = {
    val request = scala.io.Source.fromFile("src/test/resources/" + filename).mkString
    request.parseJson.convertTo[Cursor[T]]
  }
  private[this] def wrap[T: JsonFormat: reflect.ClassTag](filename: String): Wrapper[T] = {
    val request = scala.io.Source.fromFile("src/test/resources/" + filename).mkString
    request.parseJson.convertTo[Wrapper[T]]
  }
  private[this] def getQuery(url: String, map: Map[String, AnyRef] = Map()): String = {
    import spray.http._
    val stringQuery = map.map(k ⇒ (k._1, k._2.toString()))
    Uri(url).copy(query = Uri.Query(stringQuery)).toString()
  }

}

