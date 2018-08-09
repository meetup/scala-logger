package com.meetup.logging.metric

import org.scalatest.{FunSpec, Matchers}

class MetricLoggerTest extends FunSpec with Matchers {
  import MetricLoggerTest._

  private val ShouldBeLog: String => ((=> String) => Unit) = expected =>
    actual => actual shouldBe expected

  private val ShouldMatchRegexLog: String => ((=> String) => Unit) = rg =>
    actual => actual should fullyMatch regex rg

  private val Key = "test_key"

  describe("incr") {
    it("should log a counter incremented by 1 by default with no tags") {
      val logger = createLogger(ShouldBeLog(s"metric.count.$Key=1"))
      logger.incr(Key)
    }

    it("should log a counter incremented by the given value with no tags") {
      val logger = createLogger(ShouldBeLog(s"metric.count.$Key=42"))
      logger.incr(Key, 42)
    }

    it("should log a counter incremented by the given value with the given tags") {
      val logger = createLogger(ShouldBeLog(s"""metric.count.$Key=42#{"foo":"bar"}"""))
      logger.incr(Key, 42, Map("foo" -> "bar"))
    }
  }

  describe("decr") {
    it("should log a counter decremented by 1 by default with no tags") {
      val logger = createLogger(ShouldBeLog(s"metric.count.$Key=-1"))
      logger.decr(Key)
    }

    it("should log a counter decremented by the given value with no tags") {
      val logger = createLogger(ShouldBeLog(s"metric.count.$Key=-42"))
      logger.decr(Key, 42)
    }

    it("should log a counter decremented by the given value with the given tags") {
      val logger = createLogger(ShouldBeLog(s"""metric.count.$Key=-42#{"foo":"bar"}"""))
      logger.decr(Key, 42, Map("foo" -> "bar"))
    }
  }

  describe("gauge") {
    it("should log a gauge with the given value with no tags") {
      val logger = createLogger(ShouldBeLog(s"metric.gauge.$Key=42"))
      logger.gauge(Key, 42)
    }

    it("should log a gauge with the given value with tags") {
      val logger = createLogger(ShouldBeLog(s"""metric.gauge.$Key=42#{"foo":"bar"}"""))
      logger.gauge(Key, 42, Map("foo" -> "bar"))
    }
  }

  describe("set") {
    it("should log a set with the given value with no tags") {
      val logger = createLogger(ShouldBeLog(s"metric.set.$Key=42"))
      logger.set(Key, 42)
    }

    it("should log a set with the given value with tags") {
      val logger = createLogger(ShouldBeLog(s"""metric.set.$Key=42#{"foo":"bar"}"""))
      logger.set(Key, 42, Map("foo" -> "bar"))
    }
  }

  describe("timing") {
    it("should log a timing with the given value with no tags") {
      val logger = createLogger(ShouldBeLog(s"metric.timing.$Key=42"))
      logger.timing(Key, 42)
    }

    it("should log a timing with the given value with tags") {
      val logger = createLogger(ShouldBeLog(s"""metric.timing.$Key=42#{"foo":"bar"}"""))
      logger.timing(Key, 42, Map("foo" -> "bar"))
    }
  }

  describe("time") {
    it("should log a timing for the given code block with no tags") {
      // Regex to match timing value, allowing difference up to 99 milliseconds
      val logger = createLogger(ShouldMatchRegexLog(s"""^metric\\.timing\\.$Key=1[0-9][0-9]$$"""))
      logger.time(Key) {
        Thread.sleep(100L)
      }
    }

    it("should log a timing for the given code block with tags") {
      // Regex to match timing value, allowing difference up to 99 milliseconds
      val logger = createLogger(ShouldMatchRegexLog(s"""^metric\\.timing\\.$Key=1[0-9][0-9]#\\{"foo":"bar"\\}$$"""))
      logger.time(Key, Map("foo" -> "bar")) {
        Thread.sleep(100L)
      }
    }
  }

  it("allows plugging in a custom metric formatter") {
    val logger = createLogger(ShouldBeLog("test"), TestMetricFormatter)
    logger.incr(Key)
    logger.decr(Key)
    logger.gauge(Key, 42)
    logger.set(Key, 42)
    logger.timing(Key, 42)
    logger.time(Key) {
      Thread.sleep(100L)
    }
  }
}

object MetricLoggerTest {
  private object TestMetricFormatter extends MetricFormatter {
    override def apply(metric: Metric, key: String, value: String, tags: Map[String, String]): Option[String] =
      Some("test")
  }

  private def createLogger(log: (=> String) => Unit, metricFormatter: MetricFormatter = MetricFormatter): MetricLogger =
    new MetricLogger(log, metricFormatter)
}
