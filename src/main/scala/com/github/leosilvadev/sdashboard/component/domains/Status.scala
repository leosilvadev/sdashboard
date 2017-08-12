package com.github.leosilvadev.sdashboard.component.domains

import java.time.Instant

import com.github.leosilvadev.sdashboard.task.exceptions.ResponseException
import io.vertx.lang.scala.json.{Json, JsonObject}

/**
  * Created by leonardo on 7/9/17.
  */
trait Status {

  def component: Option[Component]

  def toJson: JsonObject

}

object Status {

  case class Online(comp: Component, response: JsonObject) extends Status {
    override def component: Option[Component] = Some(comp)

    override def toJson: JsonObject = Json.obj(("status", "online"), ("datetime", Instant.now().toString), ("component", comp.toJson), ("data", response))
  }

  case class Offline(comp: Component, ex: ResponseException) extends Status {
    override def component: Option[Component] = Some(comp)

    override def toJson: JsonObject = Json.obj(("status", "offline"), ("datetime", Instant.now().toString), ("component", comp.toJson), ("error", ex.message()))
  }

  case class Active() extends Status {

    override def component: Option[Component] = None

    override def toJson: JsonObject = Json.obj(("type", "active"))

  }

  def of(json: JsonObject): Option[Status] = {
    val component = Component(json.getJsonObject("component"))
    json.getString("status", "unknown") match {
      case "online" => Some(Online(component, json.getJsonObject("data")))
      case "offline" => Some(Offline(component, ResponseException(json.getString("error"))))
      case _ => None
    }
  }

}



