package com.github.leosilvadev.sdashboard.component.domains

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

    override def toJson: JsonObject = Json.obj((comp.name, "online"), ("metadata", response))
  }

  case class Offline(comp: Component, ex: ResponseException) extends Status {
    override def component: Option[Component] = Some(comp)

    override def toJson: JsonObject = Json.obj((comp.name, "offline"), ("error", ex.message()))
  }

  case class Active() extends Status {

    override def component: Option[Component] = None

    override def toJson: JsonObject = Json.emptyObj()

  }

}



