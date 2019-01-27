package com.alab.mvc

import com.alab.conf.Field
import com.alab.model.HasValues
import play.api.libs.json.{JsString, JsValue}

class JsonValues(private val value: JsValue) extends HasValues {
  override protected def _get[T](field: Field[T]): Option[T] = {
    val data = value \ field.name
    data toOption match  {
      case Some(jsString: JsString) => Some(field.dataType.fromString(jsString.value))
      case Some(jsValue: JsValue) =>
        Some(field.dataType.fromString(jsValue.toString()))
      case None => None
    }
  }
}