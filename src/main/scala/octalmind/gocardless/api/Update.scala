package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._
import octalmind.gocardless.model.WrapperProtocol._
trait Update {
  self: Api ⇒

  type UpdateRequest

  type UpdateUnmarshaller = String ⇒ Wrapper[Model]
  type UpdateMarshaller = Wrapper[UpdateRequest] ⇒ String

  def update(id: String, request: UpdateRequest)(implicit u: UpdateUnmarshaller, m: UpdateMarshaller): Future[Error \/ Model] = {
    client.put(s"$url$id", m(Wrapper(request))).map { data ⇒ data.map(u(_).entity) }
  }
}
