package com.alab.mvc.action

import com.alab.model.{HasValues, MapValues}
import com.alab.mvc.value.{JsValues, PathValue}
import javax.inject.Inject
import play.api.libs.json.JsObject
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class RequestAsJson[A](request: Request[A], json: HasValues) extends WrappedRequest[A](request)

class BodyAsJson @Inject()(val parser: BodyParsers.Default)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[RequestAsJson, AnyContent] with ActionTransformer[Request, RequestAsJson] {
  override protected def transform[A](request: Request[A]): Future[RequestAsJson[A]] = Future.successful {
    val header = request.headers.toSimpleMap
    val headersAsHasValue = MapValues(header)

    request.body match {
      case any: AnyContent => any.asJson match {
        case Some(t: JsObject) => RequestAsJson[A](request, headersAsHasValue +: JsValues(t))
        case None => RequestAsJson[A](request, headersAsHasValue)
      }
      case _ => RequestAsJson[A](request, headersAsHasValue)
    }
  }
}

class QueryAsJson @Inject()(val parser: BodyParsers.Default)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[RequestAsJson, AnyContent] with ActionTransformer[Request, RequestAsJson] {
  override protected def transform[A](request: Request[A]): Future[RequestAsJson[A]] = Future {
    val headers = MapValues(request.headers.toSimpleMap)
    val query: HasValues = PathValue(request.queryString)
    RequestAsJson[A](request, headers +: query)
  }
}

trait JsonHelper {
  def bodyAsJson(implicit parser: BodyParsers.Default, executionContext: ExecutionContext) = new BodyAsJson(parser)
}