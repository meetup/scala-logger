package com.meetup.blt.logging

import org.apache.log4j.Logger

trait Logging {
  protected val log = new AdvancedLogger(Logger.getLogger(getClass.getName))
  protected val stat = new StatsLogger(log)
}