package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._
import octalmind.gocardless.model.WrapperProtocol._

/**
 * Payment API
 */
import octalmind.gocardless.http.HttpClient
object PaymentApi {
  def apply(implicit client: HttpClient) = new PaymentApi()

}
class PaymentApi(implicit client: HttpClient) extends Api
  with List
  with Get
  with Create
  with Update
  with Cancel {

  import octalmind.gocardless.model.PaymentProtocol._
  type Model = Payment
  type CreateRequest = PaymentCreateRequest
  type UpdateRequest = PaymentUpdateRequest
  type CancelRequest = PaymentCancelRequest
  type PaymentRequestMarshaller = PaymentRetryRequest ⇒ String
  type PaymentUnMarshaller = String ⇒ Wrapper[Payment]
  def url = "/payments/"

  def retry(id: String, request: PaymentRetryRequest)(implicit m: PaymentRequestMarshaller, u: PaymentUnMarshaller): Future[Error \/ Payment] = {
    client.post(s"$url$id/actions/retry", m(request)).map { data ⇒ data.map(u(_).entity) }
  }
}
