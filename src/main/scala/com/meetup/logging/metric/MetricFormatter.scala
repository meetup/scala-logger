package com.meetup.logging.metric

import net.minidev.json.JSONObject

import scala.collection.JavaConverters.mapAsJavaMapConverter

trait MetricFormatter {
  private val invalidCharacters = "[^a-zA-Z0-9._]+"

  /**
   * Strips unsupported characters from a metric key.
   *
   * Supported characters are alpha chars, numbers, period and underscore. One the first pass each unsupported character
   * is replaced by a period. On the second pass consecutive periods are squashed into one, also periods are removed
   * from the beginning and the end of the key. If after these transformations the key becomes an empty string, `None`
   * is returned. Otherwise the cleaned key is returned.
   *
   * @param key a metric key
   * @return the cleaned key if its non-empty or `None` if the key was cleaned down to nil
   */
  def cleanKey(key: String): Option[String] = {
    val noInvalidChars = key.replaceAll(invalidCharacters, ".")
    val noExtraPeriods = noInvalidChars.replaceAll("\\.+", ".")

    val noPeriodLeft = noExtraPeriods.replaceAll("^\\.+", "")
    val noPeriodRight = noPeriodLeft.replaceAll("\\.+$", "")

    if (noPeriodRight.isEmpty) None
    else Some(noPeriodRight)
  }

  /**
   * Strips unsupported characters from metric tags.
   *
   * Supported characters are alpha chars, numbers, period and underscore. Each tag's name and value is cleaned from
   * unsupported characters with [[com.meetup.logging.metric.MetricFormatter#cleanKey cleanKey]]. If the above produces
   * an empty result for either name or value, such tag is not included into the output. Cleaned tags are converted into
   * JSON object string where keys are tags names and values are tags values. If there are no valid tags after cleaning,
   * the method returns `None`
   *
   * @param tags metric tags
   * @return a JSON string of cleaned tag names to cleaned tag values
   * @example `{"name1":"value1","name2":"value2"}`
   */
  def cleanTags(tags: Map[String, String]): Option[String] = {
    val cleanedTags = for {
      (name, value) <- tags
      cleanedName <- cleanKey(name)
      cleanedValue <- cleanKey(value)
    } yield (cleanedName, cleanedValue)

    if (cleanedTags.nonEmpty) {
      val tagsObject = new JSONObject(cleanedTags.asJava)
      Some(tagsObject.toString)
    } else None
  }

  /**
   * Applies the formatter to the metric parameters and produces a metric string.
   *
   * @param metric a metric type
   * @param key    a metric key
   * @param value  a metric value
   * @param tags   metric tags (e.g. `"bucket" -> "api"`)
   * @return a formatted metric string or `None` if the metric parameters are invalid
   */
  def apply(metric: Metric, key: String, value: String, tags: Map[String, String]): Option[String]
}

object MetricFormatter extends MetricFormatter {
  override def apply(metric: Metric, key: String, value: String, tags: Map[String, String]): Option[String] = {
    cleanKey(key).map { cleanedKey =>
      val cleanedTags = cleanTags(tags).map("#" + _).getOrElse("")

      s"metric.${metric.name}.$cleanedKey=$value$cleanedTags"
    }
  }
}
