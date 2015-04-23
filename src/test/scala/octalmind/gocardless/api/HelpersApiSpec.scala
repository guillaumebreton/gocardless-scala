
package octalmind.gocardless.api

import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent._

import octalmind.gocardless.model._
import org.joda.time._

import octalmind.gocardless.model.HelpersProtocol._
import spray.json._
import DefaultJsonProtocol._

import octalmind.gocardless.http.HttpClient
import scalaz._
import Scalaz._
import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.ExecutionContext.Implicits.global

class HelpersApiSpec extends ApiSpec {

  "get a single mandate pdf" in {
    val request = load("helpers/mandate.json")
    val jsonRequest = request.parseJson.convertTo[MandateRequest]
    val response = Array[Byte](1, 2, 3)
    val client = mock[HttpClient]
    (client.postPdf _).expects(getQuery(s"/helpers/mandate"), request, "fr").returning(Future { response.right })
    val result = Await.result(HelpersApi(client).mandate(jsonRequest, "fr"), 1.second)
    result must equal(response.right)
  }

  "check modulus" in {

    val request = load("helpers/modulus_check.json")
    val jsonRequest = request.parseJson.convertTo[ModulusCheckRequest]
    val response = ""
    val client = mock[HttpClient]
    (client.post _).expects(getQuery(s"/helpers/modulus_check"), request).returning(Future { response.right })
    val result = Await.result(HelpersApi(client).modulusCheck(jsonRequest), 1.second)
    result must equal(response.right)

  }
}
