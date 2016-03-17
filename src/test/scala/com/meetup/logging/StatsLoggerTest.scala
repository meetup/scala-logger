package com.meetup.logging

import org.scalatest.{Matchers, FunSpec}

class StatsLoggerTest extends FunSpec with Matchers {
  describe("StatsLogging") {
    describe("gauges") {
      // When delta is false value should be unsigned

      // When delta is true:
      // Value is positive, a plus sign should precede the value
      // Value is negative, a minus sign should precede the value

      it("should be unsigned") {
        val value = 1
        val result = StatsLogger.gaugeValue(value)
        result shouldBe "1"
      }

      describe("deltas") {
        it("should have a plus sign when postive") {
          val value = 1
          val result = StatsLogger.gaugeValue(value, delta = true)
          result shouldBe "+1"
        }

        it("should have a minus sign when negative") {
          val value = -1
          val result = StatsLogger.gaugeValue(value, delta = true)
          result shouldBe "-1"
        }
      }
    }
  }
}
