package octalmind.gocardless.model

import spray.json._
import org.joda.time._
import org.joda.time.format.ISODateTimeFormat

/*
 * Date time protocol to convert and read jodatime datiem
 * from Json
 */
object DateTimeProtocol extends DefaultJsonProtocol {

  implicit object DateTimeJsonFormat extends RootJsonFormat[DateTime] {
    val fmt = ISODateTimeFormat.dateTime()

    def write(d: DateTime) = {
      JsString(fmt.print(d))
    }

    def read(value: JsValue) = value match {
      case JsString(v) ⇒ fmt.parseDateTime(v)
      case _           ⇒ deserializationError("Datetime expected")
    }
  }

}
