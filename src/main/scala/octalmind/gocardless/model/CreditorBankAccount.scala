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
    account_numer: Option[String],
    bank_code: Option[String],
    country_code: Option[String],
    currency: Option[String],
    iban: Option[String],
    metadata: Option[Map[String, String]],
    links: CreditorBankAccountLinks)

  implicit var creditorBankAccountLinks = jsonFormat1(CreditorBankAccountLinks.apply)
  implicit var creditorBankAccount = jsonFormat9(CreditorBankAccount.apply)
  implicit var creditorBankAccountCreateRequest = jsonFormat8(CreditorBankAccountCreateRequest.apply)
}
