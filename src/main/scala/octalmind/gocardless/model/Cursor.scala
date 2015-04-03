
package octalmind.gocardless.model

import spray.json._
import DefaultJsonProtocol._

import spray.client.pipelining._
import spray.can.Http
import spray.json._
import spray.httpx.SprayJsonSupport._

import scala.concurrent.{ ExecutionContext, Future }
import octalmind.gocardless.Utils

object CursorProtocol extends DefaultJsonProtocol {

  case class Cursors(before: Option[String], after: Option[String])
  case class Meta(cursors: Cursors, limit: Int)
  case class Cursor[T](meta: Meta, items: List[T])

  implicit val cursorsFormat = jsonFormat2(Cursors)
  implicit val metaFormat = jsonFormat2(Meta)

  /*
   * The response format is reading a jsvalue and extract the right response using
   * the pluralized camel cased version of the parameter T
   */
  implicit def cursorReaderFormat[T: JsonFormat: reflect.ClassTag] = new RootJsonReader[Cursor[T]] {
    def read(value: JsValue) = value match {
      case x: JsObject ⇒ {
        val fieldName = Utils.getFieldName[T]
        x.fields.lift(fieldName) match {
          case Some(value) ⇒ {
            val items = value.convertTo[List[T]]
            x.fields.lift("meta").map(_.convertTo[Meta]) match {
              case Some(meta) ⇒ Cursor(meta, items)
              case None ⇒
                deserializationError("Object is missing required member meta")
            }
          }
          case None ⇒
            deserializationError("Object is missing required member '" + fieldName + "'")
        }
      }
      case u ⇒
        deserializationError("Object expected in field must be an object")
    }

  }
  implicit def cursorWriterFormat[T: JsonFormat: reflect.ClassTag] = new RootJsonWriter[Cursor[T]] {
    def write(r: Cursor[T]) = {
      val fieldName = Utils.getFieldName[T]
      JsObject(
        "meta" -> r.meta.toJson,
        fieldName -> r.items.toJson)
    }
  }
}
