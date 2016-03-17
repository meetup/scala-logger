package com.meetup.logging

import org.apache.log4j.{Logger => Log4jLogger}

trait Logging {
  protected val log = new Logger(Log4jLogger.getLogger(getClass.getName))
  protected val stat = new StatsLogger(log)
}