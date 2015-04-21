package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._

trait List {
  self: Api ⇒
  import octalmind.gocardless.model.CursorProtocol._

  type CursorUnmarshaller = String ⇒ Cursor[Model]
  def list(query: Map[String, Any])(implicit cm: CursorUnmarshaller): Future[Error \/ Cursor[Model]] = client.get(url, query).map { data ⇒ data.map(cm(_)) }
}
