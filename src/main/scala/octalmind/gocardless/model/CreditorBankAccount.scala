package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._

object CreditorBankAccountProtocol extends DefaultJsonProtocol {

  case class CreditorBankAccountLinks(
    creditor: String)
  case class CreditorBankAccount(
    id: String,
    account_holder_name: String,
    account_number_ending: String,
    bank_name: String,
    country_code: String,
    created_at: DateTime,
    currency: String,
    metadata: Option[Map[String, String]],
    links: CreditorBankAccountLinks)

  case class CreditorBankAccountCreateRequest(
    account_holder_name: String,
    account_number: Option[String],
    bank_code: Option[String],
    country_code: Option[String],
    currency: Option[String],
    iban: Option[String],
    metadata: Option[Map[String, String]],
    set_as_default_payout_account: Option[Boolean],
    links: CreditorBankAccountLinks)

  implicit val creditorBankAccountLinks = jsonFormat1(CreditorBankAccountLinks.apply)
  implicit val creditorBankAccount = jsonFormat9(CreditorBankAccount.apply)
  implicit val creditorBankAccountCreateRequest = jsonFormat9(CreditorBankAccountCreateRequest.apply)
}
