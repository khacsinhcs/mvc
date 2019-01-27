package com.alab.mvc

import com.alab.conf._
import com.alab.model.HasValues
import play.api.libs.json.JsValue

class JsValues(private val value: JsValue) extends HasValues {
    override protected def _get[T](field: Field[T]): Option[T] = {
    val js = value \ field.name
    val data = field.dataType match {
      case StringType || PhoneType || StringType => js.asOpt[String]
      case NumberType => js.asOpt[Double]
      case IntType || IdKey => js.asOpt[Int]
    }

    val tag = field.dataType.classTag.runtimeClass

    data match {
      case Some(t) if tag.isInstance(t) => Some(t.asInstanceOf[T])
      case None => {
        js.asOpt[String] match {
          case Some(str: String) => Some(field.dataType.fromString(str))
          case None => None()
        }
      }
    }
  }
}