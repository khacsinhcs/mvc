package com.alab.core

import com.alab.conf._
import com.alab.model.{HasValuesMapperHelper, MapValues}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.HashMap

class CaseClassSpec extends FlatSpec with Matchers {

  val student = MapValues(HashMap(
    "first_name" -> "Sinh",
    "age" -> "18"
  ))

  case class StudentClazz(firstName: Option[String], age: Int)

  object Student extends Type(n = "Student", des = "Student") {
    val first_name: Field[String] = f("first_name", StringType)
    val age: Field[Int] = f("age", IntType)
  }

  "hasValues" should "convert to class" in {
    val otherStudent = HasValuesMapperHelper.materialize[StudentClazz](student, Student)
    otherStudent.firstName should be(Some("Sinh"))
    val stu = MapValues(HashMap("age" -> "18"))
    stu.materialize[StudentClazz](Student).firstName should be(None)
  }

}
