package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._
import DateProtocol._
import MetadataProtocol._

object HelpersProtocol extends DefaultJsonProtocol {

  case class MandateRequest(
    account_holder_name: Option[String],
    account_number: Option[String],
    bank_code: Option[String],
    bic: Option[String],
    branch_code: Option[String],
    country_code: Option[String],
    iban: Option[String],
    mandate_reference: Option[String],
    scheme: Option[String],
    signed_at: Option[String],
    sort_code: Option[String])

  case class ModulusCheckRequest(
    account_number: Option[String],
    bank_code: Option[String],
    branch_code: Option[String],
    country_code: Option[String],
    iban: Option[String],
    sort_code: Option[String])

  implicit val mandateRequest = jsonFormat11(MandateRequest.apply)
  implicit val modulusCheckRequest = jsonFormat6(ModulusCheckRequest.apply)

}
