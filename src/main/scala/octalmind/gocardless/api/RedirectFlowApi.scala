package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._
import octalmind.gocardless.model.WrapperProtocol._

/**
 * Redirect flow API
 */
import octalmind.gocardless.http.HttpClient
object RedirectFlowApi {
  def apply(implicit client: HttpClient) = new RedirectFlowApi()

}
class RedirectFlowApi(implicit client: HttpClient) extends Api with Get with Create {

  import octalmind.gocardless.model.RedirectFlowProtocol._
  type Model = RedirectFlow
  type CreateRequest = RedirectFlowCreateRequest
  type CompleteRequestMarshaller = RedirectFlowCompleteRequest ⇒ String
  type FlowUnMarshaller = String ⇒ Wrapper[RedirectFlow]
  def url = "/redirect_flows/"

  def complete(id: String, request: RedirectFlowCompleteRequest)(implicit m: CompleteRequestMarshaller, u: FlowUnMarshaller): Future[Error \/ RedirectFlow] = {
    client.post(s"$url$id/actions/complete", m(request)).map { data ⇒ data.map(u(_).entity) }
  }
}
