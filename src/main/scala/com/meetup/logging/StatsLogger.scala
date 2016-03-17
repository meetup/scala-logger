package com.meetup.logging

import com.meetup.logging.metric.{Timing, Set, Gauge, Count}

object StatsLogger {
  def gaugeValue(value: Int, delta: Boolean = false): String = {
    // When delta is false value should be unsigned

    // When delta is true:
    // Value is positive, a plus sign should precede the value
    // Value is negative, a minus sign should precede the value
    if (delta && value > 0) {
      "+" + value.toString
    } else value.toString
  }
}

/**
 * Easy to use interface for logging metrics as json.
 * Borrowed explanations from http://statsd.readthedocs.org/en/v3.1/types.html
 */
class StatsLogger(logger: Logger) {

  /**
   * Increment a counter. Counters are the most basic and default type. They
   * are treated as a count of a type of event per second, and are typically
   * averaged over one minute.
   * @param key name of the counter.
   * @param value the amount to increment.
   * @param rate a sample rate, a float between 0 and 1. Will only send data
   *             this percentage of the time. Performed by external
   *             service if provided.
   */
  def incr(key: String, value: Int = 1, rate: Double = 1.0) = {
    logger.info(Count(key, value.toString, rate).render)
  }

  /**
   * Decrement a counter. Counters are the most basic and default type. They
   * are treated as a count of a type of event per second, and are typically
   * averaged over one minute.
   * @param key name of the counter.
   * @param value the amount to decrement.
   * @param rate a sample rate, a float between 0 and 1. Will only send data
   *             this percentage of the time. Performed by external
   *             service if provided.
   */
  def decr(key: String, value: Int = 1, rate: Double = 1.0) = {
    logger.info(Count(key, (-value).toString, rate).render)
  }

  /**
   * Gauges are a constant data type. They are not subject to averaging, and
   * they don’t change unless you change them. That is, once you set a gauge
   * value, it will be a flat line on the graph until you change it again.
   * @param key name of the gauge.
   * @param value the current value of the gauge.
   * @param rate a sample rate, a float between 0 and 1. Will only send data
   *             this percentage of the time. Performed by external
   *             service if provided.
   * @param delta whether or not to consider as a delta vs absolute value.
   */
  def gauge(key: String, value: Int, rate: Double = 1.0, delta: Boolean = false) = {
    logger.info(Gauge(key, StatsLogger.gaugeValue(value, delta), rate).render)
  }

  /**
   * Sets count the number of unique values passed to a key.
   * @param key name of the set.
   * @param value the unique value to count.
   * @param rate a sample rate, a float between 0 and 1. Will only send data
   *             this percentage of the time. Performed by external
   *             service if provided.
   */
  def set(key: String, value: Int, rate: Double = 1.0): Unit = {
    logger.info(Set(key, value.toString, rate).render)
  }

  /**
   * Timers are meant to track how long something took. They are an invaluable
   * tool for tracking application performance.
   * @param key name of the timing.
   * @param value number of milliseconds this timing took.
   * @param rate a sample rate, a float between 0 and 1. Will only send data
   *             this percentage of the time. Performed by external
   *             service if provided.
   */
  def timing(key: String, value: Int, rate: Double = 1.0): Unit = {
    logger.info(Timing(key, value.toString, rate).render)
  }

  /**
   * Convenience method for timing the given code block.
   * @param key name of the timing.
   * @param block code block to run.
   * @return
   */
  def time[A](key: String)(block: => A): A = {
    val startNano = System.nanoTime
    val result = block
    val ms = ((System.nanoTime - startNano) / 1000000).toInt
    timing(key, ms)
    result
  }

}
