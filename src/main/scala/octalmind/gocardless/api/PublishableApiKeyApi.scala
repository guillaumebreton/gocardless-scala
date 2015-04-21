package octalmind.gocardless.api

import octalmind.gocardless.http.HttpClient

/**
 * Publishable api key API
 */

object PublishableApiKeyApi {
  def apply(implicit client: HttpClient) = new PublishableApiKeyApi()
}
class PublishableApiKeyApi(implicit client: HttpClient) extends Api
  with List
  with Get
  with Create
  with Update
  with Disable {

  import octalmind.gocardless.model.PublishableApiKeyProtocol._
  type Model = PublishableApiKey
  type CreateRequest = PublishableApiKeyRequest
  type UpdateRequest = PublishableApiKeyRequest

  def url = "/publishable_api_keys/"

}
