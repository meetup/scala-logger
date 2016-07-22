# Scala Logger

[![Coverage Status](https://coveralls.io/repos/github/meetup/scala-logger/badge.svg?branch=master&t=ZUhGFK)](https://coveralls.io/github/meetup/scala-logger?branch=master)

We want to simplify the dependencies of our services when it comes to
logging and metrics.  So we think it makes the most sense to dump all
metrics to stdout and then pipe them to where ever they need to go.
This seems like a better alternative to using say one client for
NewRelic's custom metrics and another client for statsd.

So to make this simple and parseable across the board, here's a
logger you can use.  It'll add some basic metrics gather functions
for you to use.

## Usage

Implement the `com.meetup.logging.Logging` trait.  Your class will then
have access to a `log` and `stat` variable.

```scala
object MyObject extends Logging {
  def test() = {
    log.info("Logging a message")
    log.debug("Logging a debug message")

    stat.time("stat.key") {
      log.info("I'm timing this log statement.")
      Thread.sleep(100)
    }

    stat.gauge("stat.gauge.key", -2, delta = true)
  }
}
```

## Log Line Format

```{formatted timestamp} {priority} {classname}:{linenumber} - {message}```

*ex:*

 ```2016-07-22 02:07:29 INFO  PartyFavor$:145 - Queueing build for pull request...```
