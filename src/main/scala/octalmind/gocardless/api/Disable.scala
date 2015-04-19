package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._
import octalmind.gocardless.model.WrapperProtocol._
trait Disable {
  self: Api ⇒

  type DisableUnmarshaller = String ⇒ Wrapper[Model]

  def disable(id: String)(implicit u: DisableUnmarshaller): Future[Error \/ Model] = {
    client.post(s"$url$id/actions/disable", "").map { data ⇒ data.map(u(_).entity) }
  }
}
