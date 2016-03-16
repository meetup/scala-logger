package com.meetup.blt.logging

import org.apache.log4j.Logger

object LoggerMock {
  def defaultLog(s: Object) = println(s)
}

/**
 * Created by jose on 3/16/16.
 */
class LoggerMock(
    debugEnabled: Boolean = false,
    debugLog: Object => Unit = LoggerMock.defaultLog
) extends Logger("LoggerMock") {
  // The stuff we use... so far.
  override def isDebugEnabled = debugEnabled
  override def debug(message: Object) = debugLog(message)

}
