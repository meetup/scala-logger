package com.meetup.logging.metric

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FunSpec, Matchers}

class MetricFormatterTest extends FunSpec with Matchers with TableDrivenPropertyChecks {

  describe("cleaning a key") {

      def test(in: String, out: String): Unit = {
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

    it("should fail if null") {
      MetricFormatter.cleanKey(null) shouldBe None
    }
  }

  describe("cleaning tags") {
    it("should return an empty result if there are no tags") {
      MetricFormatter.cleanTags(Map.empty) shouldBe empty
    }

    it("should return a json string of cleaned tag names to cleaned tag values") {
      MetricFormatter.cleanTags(Map("key1" -> "value1", "key2" -> "value2")) shouldBe
        Some("""{"key1":"value1","key2":"value2"}""")
    }

    it("should skip tags with invalid names") {
      MetricFormatter.cleanTags(Map("key1" -> "value1", "$+%." -> "value2")) shouldBe
        Some("""{"key1":"value1"}""")
    }

    it("should skip tags with invalid values") {
      MetricFormatter.cleanTags(Map("key1" -> "#+=@", "key2" -> "value2", "key3test" -> null)) shouldBe
        Some("""{"key2":"value2"}""")
    }

    it("should return an empty result if all tags are invalid") {
      MetricFormatter.cleanTags(Map("key1" -> "#+=@", "$+%." -> "value2")) shouldBe empty
    }
  }

  describe("formatting a metric") {
    val metricTypes = Table(
      "Metric",
      Count,
      Gauge,
      Set,
      Timing
    )

    it("should return an empty result if a metric key is invalid") {
      forAll(metricTypes) { metricType =>
        MetricFormatter(metricType, "$@-..", "1", Map("bucket" -> "test")) shouldBe empty
      }
    }

    describe("without tags") {
      it("should return a cleaned metric key and original value") {
        forAll(metricTypes) { metricType =>
          MetricFormatter(metricType, "my.aw3$0m3.metric.", "foo$bar@baz", Map.empty) shouldBe
            Some(s"""metric.${metricType.name}.my.aw3.0m3.metric=foo$$bar@baz""")
        }
      }
    }

    describe("with tags") {
      it("should return a cleaned metric key, orginal value and cleaned tags") {
        forAll(metricTypes) { metricType =>
          MetricFormatter(metricType, "my.aw3$0m3.metric.", "foo$bar@baz", Map(".bucket+1" -> "test=2.")) shouldBe
            Some(s"""metric.${metricType.name}.my.aw3.0m3.metric=foo$$bar@baz#{"bucket.1":"test.2"}""")
        }
      }

      it("should not append tags if they are invalid") {
        forAll(metricTypes) { metricType =>
          MetricFormatter(metricType, "my.aw3$0m3.metric.", "foo$bar@baz", Map("$+%." -> "#+=@")) shouldBe
            Some(s"""metric.${metricType.name}.my.aw3.0m3.metric=foo$$bar@baz""")
        }
      }
    }
  }
}
