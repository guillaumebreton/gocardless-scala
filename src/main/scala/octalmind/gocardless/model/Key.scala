package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._

object ApiKeyProtocol extends DefaultJsonProtocol {

  case class ApiKey(
    id: String,
    created_at: DateTime,
    enabled: Boolean,
    key: String,
    name: String,
    webhook_url: String,
    links: Map[String, String])

  case class ApiKeyCreateRequest(
    name: Option[String],
    webhook_url: Option[String],
    links: Map[String, String])

  case class ApiKeyUpdateRequest(
    name: Option[String],
    webhook_url: Option[String])

  implicit val key = jsonFormat7(ApiKey.apply)
  implicit val keyCreateRequest = jsonFormat3(ApiKeyCreateRequest.apply)
  implicit val keyUpdateRequest = jsonFormat2(ApiKeyUpdateRequest.apply)

}
