package octalmind.gocardless

import spray.http._
import scala.concurrent.Future

import spray.json._
import octalmind.gocardless.model.WrapperProtocol._
import octalmind.gocardless.model.CursorProtocol._

object FakeHttpClient {
  def apply(json: JsValue): FakeHttpClient = {
    new FakeHttpClient(json.prettyPrint)
  }

}

class FakeHttpClient(response: String) extends HttpClient {

  var called: Boolean = false
  var callRequest: HttpRequest = null

  def pipeline: (HttpRequest) ⇒ Future[HttpResponse] = f
  def f(request: HttpRequest): Future[HttpResponse] = {
    import scala.concurrent._
    import ExecutionContext.Implicits.global
    called = true
    callRequest = request
    Future {

      val entity = HttpEntity(ContentType(MediaType.custom("application/json")), response)
      HttpResponse.apply(StatusCodes.OK, entity)
    }
  }

  def uri = callRequest.uri.toString
  def method = callRequest.method
  def entity[T: JsonFormat: reflect.ClassTag] = callRequest.entity.asString.parseJson.convertTo[Wrapper[T]]
  def close(): Unit = {}

}
