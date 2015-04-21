
package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._
import DateProtocol._
import MetadataProtocol._

object SubscriptionProtocol extends DefaultJsonProtocol {

  case class UpcomingPayment(
    charge_date: LocalDate,
    amount: Long)
  case class SubscriptionLinks(
    mandate: String)
  case class Subscription(
    id: String,
    amount: Long,
    created_at: DateTime,
    count: Option[Int],
    currency: String,
    day_of_month: Option[Int],
    end_at: Option[LocalDate],
    interval: Option[Int],
    interval_unit: String,
    metadata: Option[Map[String, String]],
    month: Option[Int],
    name: String,
    start_at: LocalDate,
    status: String,
    links: SubscriptionLinks,
    upcoming_payments: List[UpcomingPayment])

  case class SubscriptionCreateRequest(
    amount: Long,
    count: Option[Int],
    currency: String,
    day_of_month: Option[Int],
    end_at: Option[LocalDate],
    interval: Option[Int],
    interval_unit: String,
    metadata: Option[Map[String, String]],
    month: Option[Int],
    name: String,
    start_at: Option[LocalDate],
    links: SubscriptionLinks)

  case class SubscriptionUpdateRequest(
    name: Option[String],
    metadata: Map[String, String])

  case class SubscriptionCancelRequest(
    data: Metadata)

  implicit val upcomingPayment = jsonFormat2(UpcomingPayment.apply)
  implicit val links = jsonFormat1(SubscriptionLinks.apply)
  implicit val subscription = jsonFormat16(Subscription.apply)
  implicit val subscriptionCreateRequest = jsonFormat12(SubscriptionCreateRequest.apply)
  implicit val subscriptionUpdateRequest = jsonFormat2(SubscriptionUpdateRequest.apply)
  implicit val subscriptionCancelRequest = jsonFormat1(SubscriptionCancelRequest.apply)
  implicit def uCancel(e: SubscriptionCancelRequest): String = e.toJson.toString

}
