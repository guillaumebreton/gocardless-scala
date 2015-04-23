package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._
import DateProtocol._
import MetadataProtocol._

object PaymentProtocol extends DefaultJsonProtocol {

  case class PaymentLinks(
    creditor: Option[String],
    mandate: String,
    payout: Option[String],
    subscription: Option[String])

  case class Payment(
    id: String,
    amount: Double,
    amount_refunded: Option[Double],
    charge_date: Option[LocalDate],
    created_at: DateTime,
    currency: String,
    description: Option[String],
    metadata: Map[String, String],
    reference: Option[String],
    status: String,
    links: PaymentLinks)

  case class PaymentCreateRequest(
    amount: Double,
    charge_date: Option[LocalDate],
    currency: String,
    description: Option[String],
    metadata: Option[Map[String, String]],
    reference: Option[String],
    links: PaymentLinks)

  case class PaymentUpdateRequest(
    metadata: Map[String, String])

  case class PaymentCancelRequest(data: Metadata)

  case class PaymentRetryRequest(data: Metadata)

  implicit val paymentLinks = jsonFormat4(PaymentLinks.apply)
  implicit val payment = jsonFormat11(Payment.apply)
  implicit val paymentCreateRequest = jsonFormat7(PaymentCreateRequest.apply)
  implicit val paymentUpdateRequest = jsonFormat1(PaymentUpdateRequest.apply)
  implicit val paymentCancelRequest = jsonFormat1(PaymentCancelRequest.apply)
  implicit val paymentReinstateRequest = jsonFormat1(PaymentRetryRequest.apply)
  implicit def uCancel(e: PaymentCancelRequest): String = e.toJson.toString
  implicit def uRetry(e: PaymentRetryRequest): String = e.toJson.toString
}
