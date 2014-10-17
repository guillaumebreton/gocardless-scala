package octalmind.gocardless

import octalmind.gocardless.api._

object GoCardless {

  def apply(): GoCardless = new GoCardless()
}

class GoCardless {

  implicit val client: HttpClient = new SprayHttpClient()

  val customerApi = new CustomerApi()

  def close(): Unit = {
    client.close()
  }

}
