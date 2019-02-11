package com.alab.mvc.value

import com.alab.conf._
import com.alab.model.HasValues
import play.api.libs.json._

case class JsValues(private val value: JsObject) extends HasValues {

  override def getRaw(name: String): Option[_] = {
    val js = value \ name
    js match {
      case JsUndefined() => None
      case JsDefined(v) => v match {
        case obj : JsObject => Some(JsValues(obj))
        case JsBoolean(b) => Some(b)
        case JsString(s) => Some(s)
        case JsNumber(n) => Some(n)
      }
    }
  }

  protected def test[T](field: Field[T]): Option[T] = {
    val js = value \ field.name
    val data = field.dataType match {
      case StringType => js.asOpt[String]
      case PhoneType => js.asOpt[String]
      case EmailType => js.asOpt[String]
      case NumberType => js.asOpt[Double]
      case IntType => js.asOpt[Int]
      case IdKey => js.asOpt[Int]
    }


    val tag = field.dataType.classTag.runtimeClass

    data match {
      case Some(t) if tag.isInstance(t) => Some(t.asInstanceOf[T])
      case _ =>
        js.asOpt[String] match {
          case Some(str: String) => Some(field.dataType.fromString(str))
          case None => None
        }
    }
  }
}
