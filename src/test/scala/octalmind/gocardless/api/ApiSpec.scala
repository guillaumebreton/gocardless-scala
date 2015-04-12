package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.CustomerProtocol._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._
import spray.json._

import org.scalamock.scalatest.MockFactory
import spray.json._
import DefaultJsonProtocol._

trait ApiSpec extends WordSpec with MustMatchers with MockFactory {

  def load(filename: String): String = {
    scala.io.Source.fromFile("src/test/resources/" + filename).mkString.parseJson.toString
  }
  def cursor[T: JsonFormat: reflect.ClassTag](data: String): Cursor[T] = {
    data.parseJson.convertTo[Cursor[T]]
  }
  def wrap[T: JsonFormat: reflect.ClassTag](data: String): Wrapper[T] = {
    data.parseJson.convertTo[Wrapper[T]]
  }

  //user api one
  def getQuery(url: String, map: Map[String, Any] = Map()): String = {
    import spray.http._
    val stringQuery = map.map(k ⇒ (k._1, k._2.toString()))
    Uri(url).copy(query = Uri.Query(stringQuery)).toString()
  }
}

