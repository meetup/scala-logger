package com.meetup.logging

import com.meetup.logging.metric.MetricLogger
import org.apache.log4j.{Logger => Log4jLogger}

trait Logging {
  protected val log = new Logger(Log4jLogger.getLogger(getClass.getName))
  protected val metric = new MetricLogger(log.info)
}