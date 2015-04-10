package octalmind.gocardless.api

import octalmind.gocardless.HttpClient
import octalmind.gocardless.model.CustomerBankAccountProtocol._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Creditor bank account API
 */

object CustomerBankAccountApi {
  def apply(implicit client: HttpClient) = new CustomerBankAccountApi()
}
class CustomerBankAccountApi(implicit client: HttpClient)
  extends CommonApi[CustomerBankAccount, CustomerBankAccountCreateRequest, CustomerBankAccountUpdateRequest] {

  def url = "/customer_bank_accounts/%s"

  def disable(id: String): Future[CustomerBankAccount] = post(url.format(s"$id/actions/disable")).map(_.entity)

}
