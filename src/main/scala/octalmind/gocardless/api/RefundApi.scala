package octalmind.gocardless.api

/**
 * Refund API
 */
import octalmind.gocardless.http.HttpClient
object RefundApi {
  def apply(implicit client: HttpClient) = new RefundApi()

}
class RefundApi(implicit client: HttpClient) extends Api with List with Get with Create with Update {

  import octalmind.gocardless.model.RefundProtocol._
  type Model = Refund
  type CreateRequest = RefundCreateRequest
  type UpdateRequest = RefundUpdateRequest
  def url = "/refunds/"
}

