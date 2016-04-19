package com.meetup.logging

import org.apache.log4j.{Logger => Log4jLogger}

object Log4jLoggerMock {
  def defaultLog(s: Object) = println(s)
  def defaultLog(s: Object, t: Throwable) = {
    println(s"$s: with $t")
  }
}

class Log4jLoggerMock(
    debugEnabled: Boolean = false,
    debugLog: Object => Unit = Log4jLoggerMock.defaultLog
) extends Log4jLogger("LoggerMock") {
  // The stuff we use... so far.
  override def isDebugEnabled = debugEnabled
  override def debug(message: Object) = debugLog(message)
  override def debug(message: Object, throwable: Throwable) = debugLog(message)

}
