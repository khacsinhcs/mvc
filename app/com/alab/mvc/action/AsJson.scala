package com.alab.mvc.action

import com.alab.model.{HasValues, MapValues}
import com.alab.mvc.value.{JsValues, PathValue}
import play.api.libs.json.JsObject
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class RequestAsHasValue[A](request: Request[A], hasValues: HasValues) extends WrappedRequest[A](request)

class BodyReader(val parser: BodyParsers.Default)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[RequestAsHasValue, AnyContent] with ActionTransformer[Request, RequestAsHasValue] {
  override protected def transform[A](request: Request[A]): Future[RequestAsHasValue[A]] = Future.successful {
    val header = request.headers.toSimpleMap
    val headersAsHasValue = MapValues(header)

    request.body match {
      case any: AnyContent => any.asJson match {
        case Some(t: JsObject) => RequestAsHasValue[A](request, headersAsHasValue +: JsValues(t))
        case None => RequestAsHasValue[A](request, headersAsHasValue)
      }
      case _ => RequestAsHasValue[A](request, headersAsHasValue)
    }
  }
}

class QueryReader(val parser: BodyParsers.Default)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[RequestAsHasValue, AnyContent] with ActionTransformer[Request, RequestAsHasValue] {
  override protected def transform[A](request: Request[A]): Future[RequestAsHasValue[A]] = Future {
    val headers = MapValues(request.headers.toSimpleMap)
    val query: HasValues = PathValue(request.queryString)
    RequestAsHasValue[A](request, headers +: query)
  }
}

trait HasValueReader {
  def BodyAsHasValue(implicit parser: BodyParsers.Default, executionContext: ExecutionContext) = new BodyReader(parser)

  def QueryAsHasValue(implicit parser: BodyParsers.Default, executionContext: ExecutionContext) = new QueryReader(parser)
}