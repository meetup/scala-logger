package com.meetup.logging

import org.scalatest.FunSpec

class LoggingTest extends FunSpec {

  object Tester extends Logging {
    def test() = {
      log.info("Logging a message")
      log.debug("Logging a debug message")

      stat.time("stat.key") {
        log.info("I'm timing this log statement.")
        Thread.sleep(100)
      }

      stat.gauge("stat.gauge.key", -2, delta = true)
    }
  }

  it("should not spectacularly fail") {
    Tester.test()
  }

}
