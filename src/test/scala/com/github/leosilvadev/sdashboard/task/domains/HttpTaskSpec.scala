package com.github.leosilvadev.sdashboard.task.domains

import com.github.leosilvadev.sdashboard.component.domains.Component
import io.vertx.lang.scala.json.Json
import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

/**
  * Created by leonardo on 7/20/17.
  */
@RunWith(classOf[JUnitRunner])
class HttpTaskSpec extends FunSpec {

  describe("Http Task - Json") {

    it("should generate a valid JsonObject") {
      val component = Component("q1w2e3", "component 1")
      val task = HttpTask(component, "http://localhost", 1000)
      val json = Json.obj(
        ("url", "http://localhost"),
        ("frequency", 1000),
        ("headers", Json.obj())
      )
      assert(json.equals(task.toJson))
    }

    it("should parse a component from a valid JsonObject") {
      val component = Component("q1w2e3", "component 1")
      val task = HttpTask(component, "http://localhost", 1000)
      val json = Json.obj(
        ("url", "http://localhost"),
        ("frequency", 1000),
        ("headers", Json.obj())
      )
      assert(task.equals(HttpTask(component, json)))
    }

  }

}
