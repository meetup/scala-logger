package com.meetup.logging

import org.apache.log4j.{Level, Logger => Log4jLogger}

/**
 * Simplified wrapper for whatever logger we're using
 * that provides call by name methods to prevent any over anxious
 * serialization of messages.
 */
class Logger(logger: Log4jLogger) {
  def debug(message: => String) = if (logger.isDebugEnabled) logger.debug(message)
  def debug(message: => String, ex: Throwable) = if (logger.isDebugEnabled) logger.debug(message, ex)

  def info(message: => String) = if (logger.isInfoEnabled) logger.info(message)
  def info(message: => String, ex: Throwable) = if (logger.isInfoEnabled) logger.info(message, ex)

  def warn(message: => String) = if (logger.isEnabledFor(Level.WARN)) logger.warn(message)
  def warn(message: => String, ex: Throwable) = if (logger.isEnabledFor(Level.WARN)) logger.warn(message, ex)

  def error(message: => String) = if (logger.isEnabledFor(Level.ERROR)) logger.error(message)
  def error(message: => String, ex: Throwable) = if (logger.isEnabledFor(Level.ERROR)) logger.error(message, ex)

  def fatal(message: => String) = if (logger.isEnabledFor(Level.FATAL)) logger.fatal(message)
  def fatal(message: => String, ex: Throwable) = if (logger.isEnabledFor(Level.FATAL)) logger.fatal(message, ex)
}
