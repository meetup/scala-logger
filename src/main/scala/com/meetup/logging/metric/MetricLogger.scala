package com.meetup.logging.metric

/**
 * Easy to use interface for logging metrics as json.
 * Borrowed explanations from http://statsd.readthedocs.org/en/v3.1/types.html
 */
class MetricLogger(logger: (=> String) => Unit, metricFormatter: MetricFormatter) {

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
   * @param tags optional tags for this metric
   */
  def incr(key: String, value: Int = 1, tags: Map[String, String] = Map.empty): Unit = {
    metricFormatter(Count, key, value.toString, tags)
      .foreach(log)
  }

  /**
   * Decrement a counter. Counters are the most basic and default type. They
   * are treated as a count of a type of event per second, and are typically
   * averaged over one minute.
   *
   * @param key name of the counter.
   * @param value the amount to decrement.
   * @param tags optional tags for this metric
   */
  def decr(key: String, value: Int = 1, tags: Map[String, String] = Map.empty): Unit = {
    incr(key, -value, tags)
  }

  /**
   * Gauges are a constant data type. They are not subject to averaging, and
   * they don’t change unless you change them. That is, once you set a gauge
   * value, it will be a flat line on the graph until you change it again.
   *
   * @param key name of the gauge.
   * @param value the current value of the gauge.
   * @param tags optional tags for this metric
   */
  def gauge(key: String, value: Int, tags: Map[String, String] = Map.empty): Unit = {
    metricFormatter(Gauge, key, value.toString, tags)
      .foreach(log)
  }

  /**
   * Sets count the number of unique values passed to a key.
   *
   * @param key name of the set.
   * @param value the unique value to count.
   * @param tags optional tags for this metric
   */
  def set(key: String, value: Int, tags: Map[String, String] = Map.empty): Unit = {
    metricFormatter(Set, key, value.toString, tags)
      .foreach(log)
  }

  /**
   * Timers are meant to track how long something took. They are an invaluable
   * tool for tracking application performance.
   *
   * @param key name of the timing.
   * @param value number of milliseconds this timing took.
   * @param tags optional tags for this metric
   */
  def timing(key: String, value: Int, tags: Map[String, String] = Map.empty): Unit = {
    metricFormatter(Timing, key, value.toString, tags)
      .foreach(log)
  }

  /**
   * Convenience method for timing the given code block.
   *
   * @param key name of the timing.
   * @param tags optional tags for this metric
   * @param block code block to run.
   * @return the code block return value
   */
  def time[A](key: String, tags: Map[String, String] = Map.empty)(block: => A): A = {
    val start = System.currentTimeMillis
    val result = block
    val ms = (System.currentTimeMillis() - start).toInt
    timing(key, ms, tags)
    result
  }

}
