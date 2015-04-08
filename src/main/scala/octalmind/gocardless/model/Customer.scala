package octalmind.gocardless.model

import org.joda.time._
import spray.json._

import DateTimeProtocol._

object CustomerProtocol extends DefaultJsonProtocol {
  case class Customer(
    id: String,
    created_at: DateTime,
    email: String,
    given_name: String,
    family_name: String,
    address_line1: String,
    address_line2: Option[String],
    address_line3: Option[String],
    city: String,
    region: Option[String],
    postal_code: String,
    country_code: String,
    metadata: Option[Map[String, String]])

  case class CustomerRequest(
    id: Option[String] = None,
    email: Option[String] = None,
    given_name: Option[String] = None,
    family_name: Option[String] = None,
    address_line1: Option[String] = None,
    address_line2: Option[String] = None,
    address_line3: Option[String] = None,
    city: Option[String] = None,
    region: Option[String] = None,
    postal_code: Option[String] = None,
    country_code: Option[String] = None,
    metadata: Option[Map[String, String]] = None)

  implicit val customerFormat = jsonFormat13(Customer.apply)
  implicit val customerRequestFormat = jsonFormat12(CustomerRequest.apply)
}

