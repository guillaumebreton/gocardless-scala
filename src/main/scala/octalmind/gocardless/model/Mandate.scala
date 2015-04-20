package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._
import DateProtocol._
import MetadataProtocol._

object MandateProtocol extends DefaultJsonProtocol {

  case class MandateLinks(
    creditor: String,
    customer_bank_account: String)

  case class Mandate(
    id: String,
    created_at: DateTime,
    metadata: Option[Map[String, String]],
    next_possible_charge_date: LocalDate,
    reference: String,
    scheme: String,
    status: String,
    links: MandateLinks)

  case class MandateCreateRequest(
    reference: Option[String],
    scheme: Option[String],
    metadata: Option[Map[String, String]] = None,
    links: MandateLinks)

  case class MandateUpdateRequest(
    metadata: Map[String, String])

  case class MandateCancelRequest(data: Metadata)

  case class MandateReinstateRequest(data: Metadata)

  implicit val mandateLinks = jsonFormat2(MandateLinks.apply)
  implicit val mandate = jsonFormat8(Mandate.apply)
  implicit val mandateCreateRequest = jsonFormat4(MandateCreateRequest.apply)
  implicit val mandateUpdateRequest = jsonFormat1(MandateUpdateRequest.apply)
  implicit val mandateCancelRequest = jsonFormat1(MandateCancelRequest.apply)
  implicit val mandateReinstateRequest = jsonFormat1(MandateReinstateRequest.apply)
  implicit def uCancel(e: MandateCancelRequest): String = e.toJson.toString
  implicit def uReinstate(e: MandateReinstateRequest): String = e.toJson.toString
}
