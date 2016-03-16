package com.meetup.blt.logging

import org.scalatest.{Matchers, FunSpec}

class AdvancedLoggerTest extends FunSpec with Matchers {

  describe("AdvancedLogger") {
    describe("debug logging") {
      it("shouldn't log when debug is disabled") {
        val logger = new LoggerMock(
          debugEnabled = false,
          debugLog = { _ =>
          fail("Attempted to log with debug disabled")
        }
        )

        new AdvancedLogger(logger).debug("message")
      }

      it("shouldn't evaluate message when disabled") {
        val logger = new LoggerMock(debugEnabled = false)

        new AdvancedLogger(logger).debug {
          fail("Shouldn't have gotten here.")
          "message"
        }
      }

      it("should log when enabled") {
        var ran = false

        val logger = new LoggerMock(
          debugEnabled = true,
          debugLog = { _ =>
          ran = true
        })

        new AdvancedLogger(logger).debug("logging something")
        ran shouldBe true
      }

    }
  }

}
