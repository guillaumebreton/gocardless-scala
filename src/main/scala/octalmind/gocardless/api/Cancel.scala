
package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._
import octalmind.gocardless.model.WrapperProtocol._
trait Cancel {
  self: Api ⇒

  type CancelRequest

  type CancelUnmarshaller = String ⇒ Wrapper[Model]
  type CancelMarshaller = CancelRequest ⇒ String

  def cancel(id: String, request: CancelRequest)(implicit u: CancelUnmarshaller, m: CancelMarshaller): Future[Error \/ Model] = {
    client.post(s"$url$id/actions/cancel", m(request)).map { data ⇒ data.map(u(_).entity) }
  }
}
