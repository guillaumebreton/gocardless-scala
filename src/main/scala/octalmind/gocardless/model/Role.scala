
package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._

object RoleProtocol extends DefaultJsonProtocol {

  case class Permission(
    resource: String,
    access: String)
  case class Role(
    id: String,
    created_at: DateTime,
    enabled: Boolean,
    name: String,
    permissions: List[Permission])

  case class RoleCreateRequest(
    name: String,
    permissions: List[Permission])

  case class RoleUpdateRequest(
    name: Option[String],
    permissions: Option[List[Permission]])

  implicit val permission = jsonFormat2(Permission.apply)
  implicit val role = jsonFormat5(Role.apply)
  implicit val roleCreateRequest = jsonFormat2(RoleCreateRequest.apply)
  implicit val roleUpdateRequest = jsonFormat2(RoleUpdateRequest.apply)

}
