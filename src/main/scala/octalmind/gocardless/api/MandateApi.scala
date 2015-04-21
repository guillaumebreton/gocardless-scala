package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._
import octalmind.gocardless.model.WrapperProtocol._

/**
 * Mandate API
 */
import octalmind.gocardless.http.HttpClient
object MandateApi {
  def apply(implicit client: HttpClient) = new MandateApi()

}
class MandateApi(implicit client: HttpClient) extends Api
  with List
  with Get
  with Create
  with Update
  with Cancel {

  import octalmind.gocardless.model.MandateProtocol._
  type Model = Mandate
  type CreateRequest = MandateCreateRequest
  type UpdateRequest = MandateUpdateRequest
  type CancelRequest = MandateCancelRequest
  type MandateRequestMarshaller = MandateReinstateRequest ⇒ String
  type MandateUnMarshaller = String ⇒ Wrapper[Mandate]
  def url = "/mandates/"

  def reinstate(id: String, request: MandateReinstateRequest)(implicit m: MandateRequestMarshaller, u: MandateUnMarshaller): Future[Error \/ Mandate] = {
    client.post(s"$url$id/actions/reinstate", m(request)).map { data ⇒ data.map(u(_).entity) }
  }
}
