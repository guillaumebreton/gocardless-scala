package octalmind.gocardless.api

import octalmind.gocardless.http.HttpClient
import octalmind.gocardless.model.SubscriptionProtocol._
/**
 * Subscription API
 */

object SubscriptionApi {
  def apply(implicit client: HttpClient) = new SubscriptionApi()
}
class SubscriptionApi(implicit client: HttpClient) extends Api
  with List
  with Get
  with Create
  with Update
  with Cancel {

  type Model = Subscription
  type CreateRequest = SubscriptionCreateRequest
  type UpdateRequest = SubscriptionUpdateRequest
  type CancelRequest = SubscriptionCancelRequest

  def url = "/subscriptions/"
}
