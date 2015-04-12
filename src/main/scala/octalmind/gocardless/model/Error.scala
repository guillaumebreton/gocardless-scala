package octalmind.gocardless.model

import spray.json._

object ErrorProtocol extends DefaultJsonProtocol {

  case class Error(error: ErrorMessage)
  case class ErrorDetail(reason: String, message: String)
  case class ErrorMessage(message: String, errors: Array[ErrorDetail], documentation_url: String, code: Int, request_id: String)

  implicit val errorDetail = jsonFormat2(ErrorDetail.apply)
  implicit val errorMessage = jsonFormat5(ErrorMessage.apply)
  implicit val error = jsonFormat1(Error.apply)
}
