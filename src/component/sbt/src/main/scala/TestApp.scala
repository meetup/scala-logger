package com.meetup

import com.meetup.logging.Logging

object TestApp extends App with Logging {
  log.info("TestApp info")
  log.error("TestApp error")
}
