package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._

object CreditorProtocol extends DefaultJsonProtocol {

  case class CreditorLinks(
    default_eur_payout_account: Option[String],
    default_gbp_payout_account: Option[String],
    logo: Option[String])
  case class Creditor(
    id: String,
    address_line1: String,
    address_line2: Option[String],
    address_line3: Option[String],
    city: String,
    country_code: String,
    name: String,
    created_at: DateTime,
    postal_code: String,
    region: Option[String],
    links: Option[CreditorLinks])

  case class CreditorCreateRequest(
    address_line1: String,
    address_line2: Option[String],
    address_line3: Option[String],
    city: String,
    country_code: String,
    name: String,
    postal_code: String,
    region: Option[String],
    links: Option[CreditorLinks])
  case class CreditorUpdateRequest(
    address_line1: Option[String],
    address_line2: Option[String],
    address_line3: Option[String],
    city: Option[String],
    country_code: Option[String],
    name: Option[String],
    postal_code: Option[String],
    region: Option[String],
    links: Option[CreditorLinks])

  implicit val creditorLinksProtocol = jsonFormat3(CreditorLinks.apply)
  implicit val creditorProtocol = jsonFormat11(Creditor.apply)
  implicit val creditorUpdateRequestProtocol = jsonFormat9(CreditorUpdateRequest.apply)
  implicit val creditorCreateRequestProtocol = jsonFormat9(CreditorCreateRequest.apply)
}
