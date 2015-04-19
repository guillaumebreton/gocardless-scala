
package octalmind.gocardless.model

import spray.json._
import org.joda.time.LocalDate
import org.joda.time.format.ISODateTimeFormat

/*
 * Date protocol to convert and read jodatime date
 * from Json
 */
object DateProtocol extends DefaultJsonProtocol {

  implicit object DateJsonFormat extends RootJsonFormat[LocalDate] {
    val fmt = ISODateTimeFormat.localDateParser()

    def write(d: LocalDate) = {
      JsString(fmt.print(d))
    }

    def read(value: JsValue) = value match {
      case JsString(v) ⇒ LocalDate.parse(v)
      case JsNull      ⇒ null
      case x           ⇒ deserializationError(s"Datetime expected, $x found")
    }
  }

}
