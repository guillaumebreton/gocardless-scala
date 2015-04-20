package octalmind.gocardless.model

import spray.json._

object MetadataProtocol extends DefaultJsonProtocol {

  case class Metadata(
    metadata: Map[String, String])

  implicit val metadata = jsonFormat1(Metadata.apply)
}
