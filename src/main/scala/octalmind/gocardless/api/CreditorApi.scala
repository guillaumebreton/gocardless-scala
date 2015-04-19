package octalmind.gocardless.api

import octalmind.gocardless.http.HttpClient
/**
 * Creditor API
 */

object CreditorApi {
  def apply(implicit client: HttpClient) = new CreditorApi()
}
class CreditorApi(implicit client: HttpClient) extends Api with Get with Update with Create {
  import octalmind.gocardless.model.CreditorProtocol._
  type Model = Creditor
  type CreateRequest = CreditorCreateRequest
  type UpdateRequest = CreditorUpdateRequest
  def url = "/creditors/"

}
