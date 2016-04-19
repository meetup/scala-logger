package com.meetup.logging.metric

object MetricFormatter {
  private val invalidCharacters = "[^a-zA-Z0-9._]+"

  def cleanKey(key: String): Option[String] = {
    val noInvalidChars = key.replaceAll(invalidCharacters, ".")
    val noExtraPeriods = noInvalidChars.replaceAll("\\.+", ".")

    val noPeriodLeft = noExtraPeriods.replaceAll("^\\.+", "")
    val noPeriodRight = noPeriodLeft.replaceAll("\\.+$", "")

    if (noPeriodRight.isEmpty) None
    else Some(noPeriodRight)
  }

  def apply(metric: Metric, key: String, value: String): Option[String] = {
    cleanKey(key).map { cleanedKey =>
      s"metric.${metric.name}.$cleanedKey=$value"
    }
  }

}
