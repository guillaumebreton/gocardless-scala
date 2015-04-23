
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
import akka.util.ByteString

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

  val pdfType = MediaType.custom("application/pdf")

  val request = HttpRequest().withHeaders(
    GoCardlessVersion(version),
    Authorization(BasicHttpCredentials(id, key)))

  private[this] def getUri(url: String, query: Map[String, Any] = Map()): Uri = {
    Uri.from(path = url, query = Uri.Query(query.mapValues(_.toString())))
  }

  def close(): Unit = {}

  def get(url: String, query: Map[String, Any]): Future[Error \/ String] = {
    val req = request.withMethod(HttpMethods.GET).withUri(getUri(url))
    val source = Source.single(req).via(httpClient)
    source.mapAsync(convert(_)).runWith(Sink.head)
  }

  def getPdf(url: String, language: String): Future[Error \/ Array[Byte]] = {
    val lang = `Accept-Language`(Language(language))
    val accept = Accept(MediaRange(pdfType))
    val req = request.withMethod(HttpMethods.GET).withUri(getUri(url)).withHeaders(lang, accept)
    val source = Source.single(req).via(httpClient)
    source.mapAsync(convertToBytes(_)).runWith(Sink.head)
  }
  def post(url: String, body: String): Future[Error \/ String] = {
    val entity = HttpEntity(ContentType(jsonType), body)
    val req = request.withMethod(HttpMethods.POST).withUri(getUri(url)).withHeaders(Accept(jsonType))
    val source = Source.single(req)
    source.via(httpClient).mapAsync(convert(_)).runWith(Sink.head)
  }

  def postPdf(url: String, body: String, language: String): Future[Error \/ Array[Byte]] = {

    val lang = `Accept-Language`(Language(language))
    val accept = Accept(MediaRange(pdfType))
    val entity = HttpEntity(ContentType(jsonType), body)
    val req = request.withMethod(HttpMethods.POST).withUri(getUri(url)).withHeadersAndEntity(List(lang, accept), entity)
    val source = Source.single(req)
    source.via(httpClient).mapAsync(convertToBytes(_)).runWith(Sink.head)
  }

  def put(url: String, body: String): Future[Error \/ String] = {
    val accept = Accept(MediaRange(pdfType))
    val entity = HttpEntity(ContentType(jsonType), body)
    val req = request.withMethod(HttpMethods.POST).withUri(getUri(url)).withHeadersAndEntity(List(accept), entity)
    val source = Source.single(req)
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

  private[this] def convertToBytes(response: HttpResponse): Future[Error \/ Array[Byte]] = {
    response.status match {
      case StatusCodes.Success(_) ⇒ Unmarshal(response).to[ByteString].map(_.toArray.right)
      case _                      ⇒ Unmarshal(response).to[String].map(_.parseJson.convertTo[Error].left)
    }
  }

}
