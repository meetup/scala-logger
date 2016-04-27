package com.meetup.logging

import java.io.File

import org.scalatest.{FunSpec, Matchers}

import scala.sys.process.Process

class LoggingComponentTest extends FunSpec with Matchers {

  val baseDir = new File("src/component/sbt")

  it("should work when used in runtime") {
    val output = Process(Seq("sbt", "-Dsbt.log.noformat=true", "run"), baseDir).!!.trim

    output should include("TestApp Success")
  }

}
