package octalmind.gocardless.http

import scala.concurrent.Future
import scalaz._
import Scalaz._
import octalmind.gocardless.model.ErrorProtocol._

trait HttpClient {

  def close(): Unit

  def get(url: String, query: Map[String, Any]): Future[Error \/ String]

  def post(url: String, entity: String): Future[Error \/ String]

  def put(url: String, entity: String): Future[Error \/ String]

}

