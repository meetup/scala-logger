# Scala Logger
[![Build Status](https://travis-ci.org/meetup/scala-logger.svg?branch=master)](https://travis-ci.org/meetup/scala-logger)
[![Coverage Status](https://coveralls.io/repos/github/meetup/scala-logger/badge.svg?branch=master&t=ZUhGFK)](https://coveralls.io/github/meetup/scala-logger?branch=master) [ ![Download](https://api.bintray.com/packages/meetup/maven/scala-logger/images/download.svg) ](https://bintray.com/meetup/maven/scala-logger/_latestVersion)

We want to simplify the dependencies of our services when it comes to
logging and metrics.  So we think it makes the most sense to dump all
metrics to stdout and then pipe them to where ever they need to go.
This seems like a better alternative to using say one client for
NewRelic's custom metrics and another client for statsd.

So to make this simple and parseable across the board, here's a
logger you can use.  It'll add some basic metrics gather functions
for you to use.

## Adding to your project

If your project is based off of the [blt-best-sbt-docker
example](https://github.com/meetup/blt-best-sbt-docker) or was
generated from the [core-best-service
template](https://github.com/meetup/core-best-service.g8/), this
library should already be included. If not, you'll need the
following in your build.sbt:

```scala
resolvers += Resolver.bintrayRepo("meetup", "maven")
libraryDependencies += "com.meetup" %% "scala-logger" % "0.2.13"
```
See the "Download" badge above to determine the latest released version.

## Usage

Implement the `com.meetup.logging.Logging` trait.  Your class will then
have access to `log` and `metric` variables.

```scala
object MyObject extends Logging {
  def test() = {
    log.info("Logging a message")
    log.debug("Logging a debug message")

    metric.time("stat.key") {
      log.info("I'm timing this log statement.")
      Thread.sleep(100)
    }

    metric.gauge("stat.gauge.key", -2, delta = true)
  }
}
```

## Log Line Format

This library uses [log4j-jsonevent-layout](https://github.com/logstash/log4j-jsonevent-layout) as
its logging layout and logs to stdout for info and debug and to stdout for error levels. 
