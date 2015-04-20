package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._
import DateProtocol._
import MetadataProtocol._

object RedirectFlowProtocol extends DefaultJsonProtocol {

  case class RedirectFlowLinks(
    creditor: String,
    mandate: Option[String])

  case class RedirectFlow(
    id: String,
    created_at: DateTime,
    description: String,
    redirect_url: Option[String],
    scheme: Option[String],
    session_token: String,
    success_redirect_url: String,
    links: RedirectFlowLinks)

  case class RedirectFlowCreateRequest(
    description: String,
    scheme: Option[String],
    session_token: String,
    success_redirect_url: String,
    links: RedirectFlowLinks)

  case class CompleteData(
    session_token: String)
  case class RedirectFlowCompleteRequest(
    data: CompleteData)

  implicit val redirectFlowLinks = jsonFormat2(RedirectFlowLinks.apply)
  implicit val redirectFlow = jsonFormat8(RedirectFlow.apply)
  implicit val redirectFlowCreateRequest = jsonFormat5(RedirectFlowCreateRequest.apply)
  implicit val completeData = jsonFormat1(CompleteData.apply)
  implicit val redirectFlowCompleteRequest = jsonFormat1(RedirectFlowCompleteRequest.apply)

  implicit def uRetry(e: RedirectFlowCompleteRequest): String = e.toJson.toString
}
