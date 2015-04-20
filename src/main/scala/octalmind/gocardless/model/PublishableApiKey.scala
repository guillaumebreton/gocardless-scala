package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._

object PublishableApiKeyProtocol extends DefaultJsonProtocol {

  case class PublishableApiKey(
    id: String,
    created_at: DateTime,
    enabled: Boolean,
    key: String,
    name: String)

  case class PublishableApiKeyRequest(
    name: Option[String])

  implicit val publishableKey = jsonFormat5(PublishableApiKey.apply)
  implicit val publishableKeyRequest = jsonFormat1(PublishableApiKeyRequest.apply)

}
