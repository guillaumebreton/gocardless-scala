package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._

trait Get {
  self: Api ⇒
  import octalmind.gocardless.model.WrapperProtocol._
  import octalmind.gocardless.model.CursorProtocol._

  type CursorUnmarshaller = String ⇒ Cursor[Model]
  type GetUnmarshaller = String ⇒ Wrapper[Model]
  private[this] def call(url: String, query: Map[String, Any] = Map()): Future[Error \/ String] = client.get(url, query)

  def get(id: String)(implicit m: GetUnmarshaller): Future[Error \/ Model] = {
    call(s"$url$id").map { data ⇒ data.map(m(_).entity) }
  }
  def list(query: Map[String, Any])(implicit cm: CursorUnmarshaller): Future[Error \/ Cursor[Model]] = call(url, query).map { data ⇒ data.map(cm(_)) }
}
