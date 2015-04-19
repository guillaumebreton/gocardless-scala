
package octalmind.gocardless.api

import octalmind.gocardless.http.HttpClient

/**
 * Key API
 */

object ApiKeyApi {
  def apply(implicit client: HttpClient) = new ApiKeyApi()
}
class ApiKeyApi(implicit client: HttpClient) extends Api
  with Get
  with Update
  with Create
  with Disable {

  import octalmind.gocardless.model.ApiKeyProtocol._

  type Model = ApiKey
  type CreateRequest = ApiKeyCreateRequest
  type UpdateRequest = ApiKeyUpdateRequest

  def url = "/api_keys/"
}
