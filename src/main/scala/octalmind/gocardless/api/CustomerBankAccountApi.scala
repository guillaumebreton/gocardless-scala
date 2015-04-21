package octalmind.gocardless.api

import octalmind.gocardless.http.HttpClient

/**
 * Creditor bank account API
 */

object CustomerBankAccountApi {
  def apply(implicit client: HttpClient) = new CustomerBankAccountApi()
}
class CustomerBankAccountApi(implicit client: HttpClient) extends Api
  with List
  with Get
  with Create
  with Update
  with Disable {

  import octalmind.gocardless.model.CustomerBankAccountProtocol._
  type Model = CustomerBankAccount
  type CreateRequest = CustomerBankAccountCreateRequest
  type UpdateRequest = CustomerBankAccountUpdateRequest

  def url = "/customer_bank_accounts/"

}
