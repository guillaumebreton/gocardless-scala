package octalmind.gocardless.api

import octalmind.gocardless.HttpClient
import octalmind.gocardless.model.CreditorProtocol._
/**
 * Creditor API
 */

object CreditorApi {
  def apply(implicit client: HttpClient) = new CreditorApi()
}
class CreditorApi(implicit client: HttpClient) extends CommonApi[Creditor, CreditorCreateRequest, CreditorUpdateRequest] {

  def url = "/creditors/%s"

}
