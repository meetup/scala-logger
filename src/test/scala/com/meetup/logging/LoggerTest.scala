package com.meetup.logging

import org.scalatest.{Matchers, FunSpec}

class LoggerTest extends FunSpec with Matchers {

  describe("debug logging") {
    it("shouldn't log when debug is disabled") {
      val logger = new Log4jLoggerMock(
        debugEnabled = false,
        debugLog = { _ =>
        fail("Attempted to log with debug disabled")
      }
      )

      new Logger(logger).debug("message")
    }

    it("shouldn't evaluate message when disabled") {
      val logger = new Log4jLoggerMock(debugEnabled = false)

      new Logger(logger).debug {
        fail("Shouldn't have gotten here.")
        "message"
      }
    }

    it("should log when enabled") {
      var ran = false

      val logger = new Log4jLoggerMock(
        debugEnabled = true,
        debugLog = { _ =>
        ran = true
      })

      val log = new Logger(logger)

      log.debug("logging something")
      ran shouldBe true

      ran = false
      log.debug("logging something with e", new Exception())
      ran shouldBe true
    }
  }
}
