package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._
import octalmind.gocardless.model.WrapperProtocol._

/**
 * User API
 */
import octalmind.gocardless.http.HttpClient
object UserApi {
  def apply(implicit client: HttpClient) = new UserApi()

}
class UserApi(implicit client: HttpClient) extends Api
  with List
  with Get
  with Create
  with Update
  with Disable
  with Enable {

  import octalmind.gocardless.model.UserProtocol._
  type Model = User
  type CreateRequest = UserCreateRequest
  type UpdateRequest = UserUpdateRequest
  def url = "/users/"

}
