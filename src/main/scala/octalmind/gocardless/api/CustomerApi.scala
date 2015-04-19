package octalmind.gocardless.api

/**
 * Customer API
 */
import octalmind.gocardless.http.HttpClient
object CustomerApi {
  def apply(implicit client: HttpClient) = new CustomerApi()

}
class CustomerApi(implicit client: HttpClient) extends Api with Get with Create with Update {

  import octalmind.gocardless.model.CustomerProtocol._
  type Model = Customer
  type CreateRequest = CustomerRequest
  type UpdateRequest = CustomerRequest
  def url = "/customers/"
}

