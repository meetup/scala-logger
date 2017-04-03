package com.meetup.logging

import java.io.File

import org.scalatest.{FunSpec, Matchers}

import scala.sys.process.{Process, ProcessLogger}

class LoggingComponentTest extends FunSpec with Matchers {

  val baseDir = new File("src/component/sbt")
  var appJar = new File("target/scala-2.11/component-test.jar")

  it("should work when used in runtime") {
    val assemble = Process(Seq("sbt", "assembly", "run"), baseDir).!!.trim
    println(assemble)
    val proc = Process(Seq("java", "-jar", appJar.toString), baseDir)
    val (out, err) = (new StringBuffer(), new StringBuffer())
    val logger = ProcessLogger(
      out.append(_),
      err.append(_)
    )
    proc.!(logger)
    out.toString should include("TestApp info")
    err.toString should include("TestApp error")
  }

}
