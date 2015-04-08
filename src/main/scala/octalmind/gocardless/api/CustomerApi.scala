package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import spray.client.pipelining._
import spray.json._
import spray.can.Http
import spray.httpx.SprayJsonSupport._

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
class CustomerApi(implicit client: HttpClient) extends CommonApi[Customer, CustomerRequest, CustomerRequest] {

  def url = "/customers/%s"

}

