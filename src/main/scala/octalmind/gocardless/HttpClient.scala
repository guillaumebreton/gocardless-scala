package octalmind.gocardless

import akka.actor.ActorSystem
import spray.http._
import spray.client.pipelining._
import akka.io.IO
import spray.can.Http

import scala.concurrent.{ ExecutionContext, Future }
import spray.http._
import spray.http.Uri._
import com.typesafe.config.ConfigFactory

trait HttpClient {

  def pipeline: (HttpRequest) ⇒ Future[HttpResponse]

  def close(): Unit

}

class SprayHttpClient extends HttpClient {

  private[this] implicit val system = ActorSystem("gocardless-scala-client")
  import system.dispatcher

  //load configuration
  val configuration = ConfigFactory.load()
  val version = configuration.getString("gocardless.api-version")
  val key = configuration.getString("gocardless.api-key")
  val id = configuration.getString("gocardless.api-id")
  val baseUrl = configuration.getString("gocardless.base-url")

  //create the pipeline
  val pipeline = (
    addHeader("GoCardless-Version", version)
    ~> addHeader("Accept", "application/json")
    ~> addCredentials(BasicHttpCredentials(id, key))
    ~> setBaseUrl(baseUrl)
    ~> sendReceive)

  /**
   * Close the underlying system
   */
  def close(): Unit = {
    system.shutdown()
  }

  def setBaseUrl(baseUrl: String): (HttpRequest) ⇒ HttpRequest = (r: HttpRequest) ⇒ {
    r.copy(uri = Uri(baseUrl).withPath(Path(r.uri.path.toString())))
  }

}
