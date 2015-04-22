package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._
import octalmind.gocardless.model.WrapperProtocol._

/**
 * Event API
 */
import octalmind.gocardless.http.HttpClient
object EventApi {
  def apply(implicit client: HttpClient) = new EventApi()

}
class EventApi(implicit client: HttpClient) extends Api with Get with List {

  import octalmind.gocardless.model.EventProtocol._
  type Model = Event
  def url = "/events/"
}
