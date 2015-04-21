package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._
import octalmind.gocardless.model.WrapperProtocol._
trait Enable {
  self: Api ⇒

  type EnableUnmarshaller = String ⇒ Wrapper[Model]

  def enable(id: String)(implicit u: EnableUnmarshaller): Future[Error \/ Model] = {
    client.post(s"$url$id/actions/enable", "").map { data ⇒ data.map(u(_).entity) }
  }
}
