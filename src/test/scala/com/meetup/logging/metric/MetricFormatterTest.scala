package com.meetup.logging.metric

import org.scalatest.{Matchers, FunSpec}

class MetricFormatterTest extends FunSpec with Matchers {

  describe("cleaning a key") {

      def test(in: String, out: String) = {
        MetricFormatter.cleanKey(in) shouldBe Some(out)
      }

    it("should work") {
      test("my.key", "my.key")
    }

    it("should remove special characters") {
      val in = "asdf#*.asdf"
      val out = "asdf.asdf"

      test(in, out)
    }

    it("should remove duplicate periods") {
      val in = "asdf..asdf"
      val out = "asdf.asdf"

      test(in, out)
    }

    it("should trim periods on left") {
      val in = ".asdf"
      val out = "asdf"

      test(in, out)
    }

    it("should trim periods on left if multiple") {
      val in = "..asdf"
      val out = "asdf"

      test(in, out)

      val in2 = "...asdf"
      val out2 = "asdf"

      test(in2, out2)
    }

    it("should trim periods on right") {
      val in = "asdf."
      val out = "asdf"

      test(in, out)
    }

    it("should trim periods on both") {
      val in = ".asdf."
      val out = "asdf"

      test(in, out)
    }

    it("should fail for no valid characters") {
      MetricFormatter.cleanKey(".") shouldBe None
    }

    it("should fail for bunch of periods") {
      MetricFormatter.cleanKey("...") shouldBe None
    }

    it("should fail for a bunch of specials") {
      MetricFormatter.cleanKey("#!@#") shouldBe None
    }

    it("should fail if empty") {
      MetricFormatter.cleanKey("") shouldBe None
    }
  }
}
