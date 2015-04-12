package octalmind.gocardless

import octalmind.gocardless.api._
import octalmind.gocardless.http._

object GoCardless {

  def apply(): GoCardless = new GoCardless()
}

class GoCardless {

  implicit val client: HttpClient = new AkkaHttpClient()

  val customerApi = new CustomerApi()

  def close(): Unit = {
    client.close()
  }

}
