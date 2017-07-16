package com.github.leosilvadev.sdashboard.component.domains

import io.vertx.lang.scala.json.JsonObject

/**
  * Created by leonardo on 7/9/17.
  */
trait Status {

  def component: Option[Component]

}

object Status {

  case class Online(comp: Component, response: JsonObject) extends Status {
    override def component: Option[Component] = Some(comp)
  }

  case class Offline(comp: Component, ex: Throwable) extends Status {
    override def component: Option[Component] = Some(comp)
  }

  case class Active() extends Status {

    override def component: Option[Component] = None

  }

}



