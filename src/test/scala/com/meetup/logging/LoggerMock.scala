package com.meetup.logging

import org.apache.log4j.{Logger => Log4jLogger}

object LoggerMock {
  def defaultLog(s: Object) = println(s)
}

/**
 * Created by jose on 3/16/16.
 */
class LoggerMock(
    debugEnabled: Boolean = false,
    debugLog: Object => Unit = LoggerMock.defaultLog
) extends Log4jLogger("LoggerMock") {
  // The stuff we use... so far.
  override def isDebugEnabled = debugEnabled
  override def debug(message: Object) = debugLog(message)

}
