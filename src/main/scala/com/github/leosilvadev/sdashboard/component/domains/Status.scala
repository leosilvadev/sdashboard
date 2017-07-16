package com.github.leosilvadev.sdashboard.component.domains

import io.vertx.lang.scala.json.JsonObject

/**
  * Created by leonardo on 7/9/17.
  */
trait Status {}

object Status {

  case class Online(response: JsonObject) extends Status

  case class Offline(ex: Throwable) extends Status

  case class Active() extends Status

}



