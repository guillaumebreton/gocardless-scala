package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import octalmind.gocardless.model.CursorProtocol._
import octalmind.gocardless.model.CustomerProtocol._
import octalmind.gocardless.model.WrapperProtocol._

trait CommonApi[U, T, V] extends Api[U, T, V] {
  def url: String

  def query(query: Map[String, Any]): Future[Cursor[U]] = list(url.format(""), query)

  def retrieve(id: String): Future[U] =
    get(url.format(id)).map(_.entity)

  def update(id: String, request: V): Future[U] =
    put(url.format(id), request).map(_.entity)

  def create(request: T): Future[U] =
    post(url.format(""), request).map(_.entity)
}
