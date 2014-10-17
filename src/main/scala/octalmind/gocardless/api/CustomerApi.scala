package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import spray.client.pipelining._
import spray.json._
import spray.can.Http
import spray.httpx.SprayJsonSupport._
import octalmind.gocardless.resources._
import octalmind.gocardless._

import octalmind.gocardless.HttpClient
import octalmind.gocardless.model.CursorProtocol._
import octalmind.gocardless.model.CustomerProtocol._
import octalmind.gocardless.model.WrapperProtocol._

import org.joda.time._

/**
 * Customer API
 */

object CustomerApi {
  def apply(implicit client: HttpClient) = new CustomerApi()

}
class CustomerApi(implicit client: HttpClient) extends Api[Customer, CustomerRequest] {

  val url = "/customers/%s"

  def query(query: Map[String, AnyRef]): Future[Cursor[Customer]] = list(url.format(""), query)

  def retrieve(id: String): Future[Customer] =
    get(url.format(id)).map(_.entity)

  def update(id: String, request: CustomerRequest): Future[Customer] =
    put(url.format(id), request).map(_.entity)

  def create(request: CustomerRequest): Future[Customer] =
    post(url.format(""), request).map(_.entity)

}

