package octalmind.gocardless.api

import octalmind.gocardless.http.HttpClient
import octalmind.gocardless.model.WrapperProtocol._

abstract class Api(implicit val client: HttpClient) {
  type Model
  def url: String
}
