package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._
import DateProtocol._
import MetadataProtocol._

object EventProtocol extends DefaultJsonProtocol {

  case class EventLinks(
    mandate: Option[String],
    new_customer_bank_account: Option[String],
    parent_event: Option[String],
    payment: Option[String],
    payout: Option[String],
    previous_customer_bank_account: Option[String],
    refund: Option[String],
    subscription: Option[String])
  case class EventDetails(
    cause: Option[String],
    description: Option[String],
    origin: Option[String],
    reason_code: Option[String],
    report_type: Option[String],
    scheme: Option[String])
  case class Event(
    id: String,
    action: String,
    created_at: DateTime,
    details: EventDetails,
    metadata: Map[String, String],
    links: EventLinks)

  implicit val eventLinks = jsonFormat8(EventLinks.apply)
  implicit val eventDetails = jsonFormat6(EventDetails.apply)
  implicit val event = jsonFormat6(Event.apply)

}
