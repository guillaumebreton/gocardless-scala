
package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._

object CustomerBankAccountProtocol extends DefaultJsonProtocol {

  case class CustomerBankAccountLinks(
    customer: String,
    customer_bank_account_token: Option[String])
  case class CustomerBankAccount(
    id: String,
    account_holder_name: String,
    account_number_ending: String,
    bank_name: String,
    country_code: String,
    created_at: DateTime,
    currency: String,
    enabled: Boolean,
    metadata: Option[Map[String, String]],
    links: CustomerBankAccountLinks)

  case class CustomerBankAccountCreateRequest(
    account_holder_name: String,
    account_number: Option[String],
    branch_code: Option[String],
    bank_name: Option[String],
    country_code: Option[String],
    currency: Option[String],
    iban: Option[String],
    metadata: Option[Map[String, String]],
    links: CustomerBankAccountLinks)

  case class CustomerBankAccountUpdateRequest(
    metadata: Map[String, String])

  implicit val customerBankAccountLinks = jsonFormat2(CustomerBankAccountLinks.apply)
  implicit val customerBankAccount = jsonFormat10(CustomerBankAccount.apply)
  implicit val customerBankAccountCreateRequest = jsonFormat9(CustomerBankAccountCreateRequest.apply)
  implicit val customerBankAccountUpdateRequest = jsonFormat1(CustomerBankAccountUpdateRequest.apply)

}
