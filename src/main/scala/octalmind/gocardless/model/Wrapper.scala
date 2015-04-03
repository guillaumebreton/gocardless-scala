package octalmind.gocardless.model

import spray.json._
import DefaultJsonProtocol._
import octalmind.gocardless.Utils

object WrapperProtocol extends DefaultJsonProtocol {
  case class Wrapper[T](entity: T)
  implicit def updateRequestFormat[T: JsonFormat: reflect.ClassTag] = new RootJsonWriter[Wrapper[T]] {
    def write(r: Wrapper[T]) = {
      val fieldName = Utils.getFieldName[T]
      JsObject {
        fieldName -> r.entity.toJson
      }
    }
  }
  implicit def updateResquestFormat[T: JsonFormat: reflect.ClassTag] = new RootJsonReader[Wrapper[T]] {
    def read(value: JsValue) = value match {
      case x: JsObject ⇒ {
        val fieldName = Utils.getFieldName[T]
        x.fields.lift(fieldName) match {
          case Some(value) ⇒ Wrapper(value.convertTo[T])
          case None ⇒
            deserializationError("Object is missing required member '" + fieldName + "'")
        }
      }
      case u ⇒
        deserializationError("Object expected in field must be an object")
    }

  }
}
