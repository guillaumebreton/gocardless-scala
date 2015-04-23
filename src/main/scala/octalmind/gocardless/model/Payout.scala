package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._
import DateProtocol._
import MetadataProtocol._

object PayoutProtocol extends DefaultJsonProtocol {

  case class PayoutLinks(
    creditor: String,
    creditor_bank_account: String)
  case class Payout(
    id: String,
    amount: Double,
    created_at: DateTime,
    currency: String,
    reference: Option[String],
    status: String,
    links: PayoutLinks)

  implicit val payoutLinks = jsonFormat2(PayoutLinks.apply)
  implicit val payout = jsonFormat7(Payout.apply)
}
