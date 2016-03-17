package com.meetup.logging.metric

/**
 * Object representation of Metrics with some methods to serialize them
 * to Json.
 */
abstract class Stat(metric: String, key: String, value: String, rate: Double) {
  /**
   * Because escaping quote in string interpolation is apparently a known bug?
   * https://issues.scala-lang.org/browse/SI-6476
   */
  private def quote(s: String): String = {
    "\"" + s + "\""
  }

  def toJsonValueMap: Map[String, String] = {
    Map(
      "metric" -> quote(metric),
      "key" -> quote(key),
      "rate" -> rate.toString,
      "value" -> quote(value.toString)
    )
  }

  def render: String = {
    val innerJson = toJsonValueMap.map {
      case (k, v) =>
        val jsonKey = quote(k)
        s"$jsonKey:$v"
    }.mkString(",")

    s"{$innerJson}"
  }
}

case class Count(
  key: String,
  value: String,
  rate: Double
) extends Stat("count", key, value, rate)

case class Gauge(
  key: String,
  value: String,
  rate: Double
) extends Stat("gauge", key, value, rate)

case class Set(
  key: String,
  value: String,
  rate: Double
) extends Stat("set", key, value, rate)

case class Timing(
  key: String,
  value: String,
  rate: Double
) extends Stat("timing", key, value, rate)
