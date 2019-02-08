package com.alab.mvc.model

import com.alab.model.HasValues

case class PathValue(map: Map[String, Seq[String]]) extends HasValues {
  override def getRaw(name: String): Option[_] = {
    map.get(name) match {
      case Some(List(s)) => Some(s)
      case None => None
      case Some(l) => Some(l)
    }
  }
}
