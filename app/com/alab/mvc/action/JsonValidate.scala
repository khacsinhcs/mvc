package com.alab.mvc.action

import com.alab.conf.Type
import com.alab.conf.validate.{Validate, ValidateFail, ValidateSuccess}
import com.alab.model.HasValues
import play.api.mvc.{ActionFilter, Result, Results}

import scala.concurrent.{ExecutionContext, Future}

class JsonValidate(block: HasValues => Validate[String])(implicit override val executionContext: ExecutionContext, kind: Type) extends ActionFilter[RequestAsHasValue] {
  override protected def filter[A](request: RequestAsHasValue[A]): Future[Option[Result]] = Future.successful {
    val validated = request.hasValues validate kind
    validated match {
      case ValidateSuccess() =>
        block(request.hasValues) match {
          case ValidateSuccess() => None
          case ValidateFail(s) => Some(Results.BadRequest(s))
        }
      case ValidateFail(ls) => Some(Results.BadRequest(ls.mkString(",")))
    }
  }

}

trait RequestValidator {
  def Validate(kind: Type)(implicit executionContext: ExecutionContext): JsonValidate = Validate(executionContext, kind)

  def Validate(implicit executionContext: ExecutionContext, kind: Type): JsonValidate = new JsonValidate({ _: HasValues =>
    ValidateSuccess()
  })

  def Validate(block: HasValues => Validate[String])(implicit executionContext: ExecutionContext, kind: Type) = new JsonValidate(block)
}
