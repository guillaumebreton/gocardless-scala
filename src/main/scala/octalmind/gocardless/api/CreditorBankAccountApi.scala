

package octalmind.gocardless.api

import octalmind.gocardless.HttpClient
import octalmind.gocardless.model.CreditorBankAccountProtocol._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Creditor bank account API
 */

object CreditorBankAccountApi {
  def apply(implicit client: HttpClient) = new CreditorBankAccountApi()
}
class CreditorBankAccountApi(implicit client: HttpClient)
  extends CommonApi[CreditorBankAccount, CreditorBankAccountCreateRequest, CreditorBankAccountCreateRequest] {

  def url = "/creditor_bank_accounts/%s"

  def disable(id: String): Future[CreditorBankAccount] = post(url.format(s"$id/actions/disable")).map(_.entity)

}
