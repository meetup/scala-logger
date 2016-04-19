package com.meetup.logging.metric

sealed abstract class Metric(val name: String)

case object Count extends Metric("count")
case object Gauge extends Metric("gauge")
case object Set extends Metric("set")
case object Timing extends Metric("timing")

