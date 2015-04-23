package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._
import octalmind.gocardless.model.WrapperProtocol._

/**
 * Helpers API
 */
import octalmind.gocardless.http.HttpClient
object HelpersApi {
  def apply(implicit client: HttpClient) = new HelpersApi()

}
class HelpersApi(implicit client: HttpClient) {
  import octalmind.gocardless.model.HelpersProtocol._

  def mandate(request: MandateRequest, language: String): Future[Error \/ Array[Byte]] = {
    client.postPdf("/helpers/mandate", request.toJson.toString, language)
  }

  def modulusCheck(request: ModulusCheckRequest): Future[Error \/ String] = {
    client.post("/helpers/modulus_check", request.toJson.toString)

  }
}
