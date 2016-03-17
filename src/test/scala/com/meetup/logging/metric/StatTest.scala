package com.meetup.logging.metric

import org.json4s.JObject
import org.json4s.JsonAST.{JBool, JDouble, JString}
import org.json4s.native.JsonMethods._
import org.scalatest.{Matchers, FunSpec}

class StatTest extends FunSpec with Matchers {

  describe("Count") {
    describe("incr") {
      it("should serialize to valid json") {
        val result = Count("test.key", 1, 1.0).render
        parse(result) // Will throw exception on failure.
      }

      it("should trim json response") {
        val result = Count("test.key", 1, 1.0).render
        val expected = compact(render(parse(result)))

        result shouldBe expected
      }

      it("should render expected fields") {
        val key = "test.key"
        val count = 1
        val rate = 0.8

        val result = Count(key, count, rate).render
        val json = parse(result)

        json shouldBe a[JObject]

        val jsonKey = json \ "key"
        jsonKey match {
          case JString(s) => s shouldBe key
          case other => fail(s"Received unexpected type: $other")
        }

        val jsonRate = json \ "rate"
        jsonRate match {
          case JDouble(d) => d shouldBe rate
          case other => fail(s"Received unexpected type: $other")
        }
      }
    }
  }

  describe("Gauge") {
    it("should serialize to valid json") {
      val render = Gauge("test.key", 1, 1.0, false).render
      parse(render) // Will throw exception on failure.
    }

    it("should trim json response") {
      val result = Gauge("test.key", 1, 1.0, false).render
      val expected = compact(render(parse(result)))

      result shouldBe expected
    }

    it("should render expected fields") {
      val delta = true
      val key = "test.key"

      val result = Gauge(key, 1, 1.0, delta).render
      val json = parse(result)

      json shouldBe a[JObject]

      val jsonDelta = json \ "delta"
      jsonDelta match {
        case JBool(b) => b shouldBe delta
        case other => fail(s"Received unexpected type: $other")
      }
    }
  }

}
