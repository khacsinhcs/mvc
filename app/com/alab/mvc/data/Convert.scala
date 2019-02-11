package com.alab.mvc.data

import com.alab.conf._
import com.alab.model.HasValues
import com.alab.mvc.value.JsValues
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._

object Convert {
  def toJson(value: HasValues, t: Type): JsObject = {
    def reconstruct(value: HasValues): JsObject = {
      val seq = t.fields.map(f => {
        val jsValue: JsValueWrapper = {
          val data = value -> f
          data match {
            case Some(data: String) => JsString(data)
            case Some(data: Double) => JsNumber(data)
            case Some(data: Int) => JsNumber(data)
            case _ => JsNull
          }
        }
        (f.name, jsValue)
      }).toArray
      Json.obj(seq: _*)
    }
    value match {
      case JsValues(v: JsObject) => v
      case other => reconstruct(other)
    }

  }
}
