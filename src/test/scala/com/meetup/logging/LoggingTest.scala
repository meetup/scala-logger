package com.meetup.logging

import org.scalatest.FunSpec

class LoggingTest extends FunSpec {

  object Tester extends Logging {
    def test() = {
      log.info("Logging a message")
      log.debug("Logging a debug message")

      metric.time("mystat.subkey") {
        log.info("I'm timing this log statement.")
        Thread.sleep(100)
      }

      metric.gauge("mystat.subkey", -2)
    }
  }

  it("should not spectacularly fail") {
    Tester.test()
  }

}
