package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._

object RefundProtocol extends DefaultJsonProtocol {
  case class RefundLinks(
    payment: String)
  case class Refund(
    id: String,
    amount: Long,
    currency: String,
    created_at: DateTime,
    metadata: Option[Map[String, String]],
    links: RefundLinks)

  case class RefundCreateRequest(
    amount: Long,
    total_amount_confirmation: Long,
    metadata: Option[Map[String, String]] = None,
    links: RefundLinks)
  case class RefundUpdateRequest(
    metadata: Option[Map[String, String]] = None)

  implicit val refundLinksformat = jsonFormat1(RefundLinks.apply)
  implicit val refundFormat = jsonFormat6(Refund.apply)
  implicit val refundCreateRequestFormat = jsonFormat4(RefundCreateRequest.apply)
  implicit val refundUpdateRequestFormat = jsonFormat1(RefundUpdateRequest.apply)
}

