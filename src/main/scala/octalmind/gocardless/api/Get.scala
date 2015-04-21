package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._

trait Get {
  self: Api ⇒
  import octalmind.gocardless.model.WrapperProtocol._

  type GetUnmarshaller = String ⇒ Wrapper[Model]

  def get(id: String)(implicit m: GetUnmarshaller): Future[Error \/ Model] = {
    client.get(s"$url$id", Map()).map { data ⇒ data.map(m(_).entity) }
  }
}
