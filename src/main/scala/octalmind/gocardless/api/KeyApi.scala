
package octalmind.gocardless.api

import octalmind.gocardless.HttpClient
import octalmind.gocardless.model.ApiKeyProtocol._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Key API
 */

object KeyApi {
  def apply(implicit client: HttpClient) = new KeyApi()
}
class KeyApi(implicit client: HttpClient) extends CommonApi[ApiKey, ApiKeyCreateRequest, ApiKeyUpdateRequest] {

  def url = "/api_keys/%s"

  def disable(id: String): Future[ApiKey] = post(url.format(s"$id/actions/disable")).map(_.entity)

}
