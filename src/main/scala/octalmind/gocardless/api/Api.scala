package octalmind.gocardless.api

import org.joda.time._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import spray.client.pipelining._
import spray.http._
import spray.httpx.unmarshalling.{ Unmarshaller, FromResponseUnmarshaller }
import spray.json._

import octalmind.gocardless.HttpClient

abstract class Api[U: JsonFormat: Manifest, T: JsonFormat: Manifest](implicit client: HttpClient) {

  import octalmind.gocardless.model._
  import octalmind.gocardless.model.WrapperProtocol._
  import octalmind.gocardless.model.CursorProtocol._

  import spray.httpx.SprayJsonSupport._
  import spray.json.DefaultJsonProtocol._

  // val urlFormatter = s"$baseUrl/%s"

  def p = client.pipeline ~> unmarshal[Wrapper[U]]

  def cursor = client.pipeline ~> unmarshal[Cursor[U]]

  def get(url: String): Future[Wrapper[U]] = {
    p(Get(url))
  }

  def list(url: String, query: Map[String, AnyRef]): Future[Cursor[U]] = {
    val stringQuery = query.map(k ⇒ (k._1, k._2.toString()))
    val uri = Uri(url).copy(query = Uri.Query(stringQuery))
    cursor(Get(uri))
  }

  def put(url: String, request: T): Future[Wrapper[U]] = p(Put(url, Wrapper[T](request)))

  def post(url: String, request: T): Future[Wrapper[U]] = p(Post(url, Wrapper[T](request)))

}
