package octalmind.gocardless.api

import octalmind.gocardless.http.HttpClient
import octalmind.gocardless.model.RoleProtocol._
/**
 * Role API
 */

object RoleApi {
  def apply(implicit client: HttpClient) = new RoleApi()
}
class RoleApi(implicit client: HttpClient) extends Api
  with List
  with Get
  with Create
  with Update
  with Disable {

  type Model = Role
  type CreateRequest = RoleCreateRequest
  type UpdateRequest = RoleUpdateRequest

  def url = "/roles/"
}
