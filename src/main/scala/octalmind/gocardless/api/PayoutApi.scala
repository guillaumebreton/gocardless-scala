package octalmind.gocardless.api

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._
import octalmind.gocardless.model.WrapperProtocol._

/**
 * Payout API
 */
import octalmind.gocardless.http.HttpClient
object PayoutApi {
  def apply(implicit client: HttpClient) = new PayoutApi()

}
class PayoutApi(implicit client: HttpClient) extends Api with Get with List {

  import octalmind.gocardless.model.PayoutProtocol._
  type Model = Payout
  def url = "/payouts/"
}
