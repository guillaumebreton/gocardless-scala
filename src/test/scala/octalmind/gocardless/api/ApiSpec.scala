package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import octalmind.gocardless.FakeHttpClient
import org.joda.time._

import octalmind.gocardless.model.CustomerProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._
import spray.http.HttpMethods

trait ApiSpec extends WordSpec with MustMatchers {

  def cursor[T: JsonFormat: reflect.ClassTag](filename: String): Cursor[T] = {
    val request = scala.io.Source.fromFile("src/test/resources/" + filename).mkString
    request.parseJson.convertTo[Cursor[T]]
  }
  def wrap[T: JsonFormat: reflect.ClassTag](filename: String): Wrapper[T] = {
    val request = scala.io.Source.fromFile("src/test/resources/" + filename).mkString
    request.parseJson.convertTo[Wrapper[T]]
  }
  def getQuery(url: String, map: Map[String, Any] = Map()): String = {
    import spray.http._
    val stringQuery = map.map(k ⇒ (k._1, k._2.toString()))
    Uri(url).copy(query = Uri.Query(stringQuery)).toString()
  }
}

