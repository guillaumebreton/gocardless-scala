
package octalmind.gocardless.http
import akka.actor.ActorSystem
import akka.http.Http
import akka.stream.{ FlowMaterializer, ActorFlowMaterializer }
import akka.http.model._
import akka.stream.scaladsl._
import akka.stream.scaladsl.Source
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.typesafe.config.ConfigFactory
import akka.http.model.headers._
import akka.http.unmarshalling._
import scalaz.{ \/, Scalaz }
import Scalaz._
import spray.json._
import DefaultJsonProtocol._
import octalmind.gocardless.model.ErrorProtocol._

object GoCardlessVersion {
  def apply(version: String): GoCardlessVersion = new GoCardlessVersion(version)
}
class GoCardlessVersion(version: String) extends CustomHeader {
  def name(): String = "GoCardless-Version"
  def value(): String = version

}

class AkkaHttpClient extends HttpClient {
  implicit val system = ActorSystem("gocardless-system")

  implicit val materializer = ActorFlowMaterializer()
  val configuration = ConfigFactory.load()
  val version = configuration.getString("gocardless.api-version")
  val key = configuration.getString("gocardless.api-key")
  val id = configuration.getString("gocardless.api-id")
  val baseUrl = configuration.getString("gocardless.base-url")

  val httpClient = Http(system).outgoingConnection(baseUrl, 80)

  val jsonType = MediaType.custom("application/json")

  val request = HttpRequest().withHeaders(
    GoCardlessVersion(version),
    Authorization(BasicHttpCredentials(id, key)),
    Accept(MediaRange(jsonType)))

  private[this] def getUri(url: String, query: Map[String, Any] = Map()): Uri = {
    Uri.from(path = url, query = Uri.Query(query.mapValues(_.toString())))
  }

  def close(): Unit = {}

  def get(url: String, query: Map[String, Any]): Future[Error \/ String] = {
    val source = Source.single(HttpRequest(uri = getUri(url, query))).via(httpClient)
    source.mapAsync(convert(_)).runWith(Sink.head)
  }

  def post(url: String, entity: String): Future[Error \/ String] = {
    val request = HttpRequest(method = HttpMethods.POST, uri = getUri(url), entity = HttpEntity(ContentType(jsonType), entity))
    val source = Source.single(request)
    source.via(httpClient).mapAsync(convert(_)).runWith(Sink.head)
  }

  def put(url: String, entity: String): Future[Error \/ String] = {
    val request = HttpRequest(method = HttpMethods.PUT, uri = getUri(url), entity = HttpEntity(ContentType(jsonType), entity))
    val source = Source.single(request)
    source.via(httpClient).mapAsync(convert(_)).runWith(Sink.head)
  }

  private[this] def convert(response: HttpResponse): Future[Error \/ String] = {
    Unmarshal(response).to[String].map { data ⇒
      response.status match {
        case StatusCodes.Success(_) ⇒ data.right
        case _                      ⇒ data.parseJson.convertTo[Error].left
      }
    }
  }

}
