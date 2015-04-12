package octalmind.gocardless.api

import octalmind.gocardless.http.HttpClient
import octalmind.gocardless.model.CreditorBankAccountProtocol._
/**
 * Creditor bank account API
 */

object CreditorBankAccountApi {
  def apply(implicit client: HttpClient) = new CreditorBankAccountApi()
}
class CreditorBankAccountApi(implicit client: HttpClient) extends Api with Get with Create with Disable {

  type Model = CreditorBankAccount
  type CreateRequest = CreditorBankAccountCreateRequest

  def url = "/creditor_bank_accounts/"
}
