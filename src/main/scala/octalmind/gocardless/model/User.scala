
package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._
import DateProtocol._
import MetadataProtocol._

object UserProtocol extends DefaultJsonProtocol {

  case class UserLinks(
    role: String)
  case class User(
    id: String,
    created_at: DateTime,
    email: String,
    enabled: Boolean,
    family_name: String,
    given_name: String,
    links: UserLinks)

  case class UserCreateRequest(
    email: String,
    family_name: String,
    given_name: String,
    password: String,
    password_confirmation: Option[String],
    links: UserLinks)

  case class UserUpdateRequest(
    email: Option[String] = None,
    family_name: Option[String] = None,
    given_name: Option[String] = None,
    password: Option[String] = None,
    password_confirmation: Option[String] = None,
    links: Option[UserLinks] = None)

  implicit val links = jsonFormat1(UserLinks.apply)
  implicit val user = jsonFormat7(User.apply)
  implicit val userCreateRequest = jsonFormat6(UserCreateRequest.apply)
  implicit val userUpdateRequest = jsonFormat6(UserUpdateRequest.apply)

}
