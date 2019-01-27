package com.alab.mvc.data

import com.alab.conf._
import com.alab.model.HasValues
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._

object Convert {
  def toJson(value: HasValues, t: Type): JsObject = {
    val seq = t.fields.map(f => {
      val jsValue: JsValueWrapper = {
        val dataInValue = value -> f
        dataInValue match {
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
}
