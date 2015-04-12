package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._
import octalmind.gocardless.model.WrapperProtocol._
trait Create {
  self: Api ⇒

  type CreateRequest
  type CreateMarshaller = Wrapper[CreateRequest] ⇒ String
  type CreateUnmarshaller = String ⇒ Wrapper[Model]

  def create(request: CreateRequest)(implicit u: CreateUnmarshaller, m: CreateMarshaller): Future[Error \/ Model] = {
    val requestString: String = m(Wrapper(request))
    client.post(url, requestString).map { data ⇒ data.map(u(_).entity) }
  }
}
