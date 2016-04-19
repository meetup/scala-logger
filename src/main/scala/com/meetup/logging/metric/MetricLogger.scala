package com.meetup.logging.metric

import com.meetup.logging.Logger

/**
 * Easy to use interface for logging metrics as json.
 * Borrowed explanations from http://statsd.readthedocs.org/en/v3.1/types.html
 */
class MetricLogger(logger: (=> String) => Unit) {

  private def log(out: String) {
    logger(out)
  }

  /**
   * Increment a counter. Counters are the most basic and default type. They
   * are treated as a count of a type of event per second, and are typically
   * averaged over one minute.
   *
   * @param key name of the counter.
   * @param value the amount to increment.
   */
  def incr(key: String, value: Int = 1) {
    MetricFormatter(Count, key, value.toString)
      .foreach(log)
  }

  /**
   * Decrement a counter. Counters are the most basic and default type. They
   * are treated as a count of a type of event per second, and are typically
   * averaged over one minute.
   *
   * @param key name of the counter.
   * @param value the amount to decrement.
   */
  def decr(key: String, value: Int = 1) {
    incr(key, -value)
  }

  /**
   * Gauges are a constant data type. They are not subject to averaging, and
   * they don’t change unless you change them. That is, once you set a gauge
   * value, it will be a flat line on the graph until you change it again.
   *
   * @param key name of the gauge.
   * @param value the current value of the gauge.
   */
  def gauge(key: String, value: Int) {
    MetricFormatter(Gauge, key, value.toString)
      .foreach(log)
  }

  /**
   * Sets count the number of unique values passed to a key.
   *
   * @param key name of the set.
   * @param value the unique value to count.
   */
  def set(key: String, value: Int): Unit = {
    MetricFormatter(Set, key, value.toString)
      .foreach(log)
  }

  /**
   * Timers are meant to track how long something took. They are an invaluable
   * tool for tracking application performance.
   *
   * @param key name of the timing.
   * @param value number of milliseconds this timing took.
   */
  def timing(key: String, value: Int): Unit = {
    MetricFormatter(Timing, key, value.toString)
      .foreach(log)
  }

  /**
   * Convenience method for timing the given code block.
   *
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
