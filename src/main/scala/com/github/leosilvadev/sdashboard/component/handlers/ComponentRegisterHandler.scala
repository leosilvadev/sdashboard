package com.github.leosilvadev.sdashboard.component.handlers

import com.github.leosilvadev.sdashboard.Events
import com.github.leosilvadev.sdashboard.component.domains.Component
import com.github.leosilvadev.sdashboard.util.Response
import io.vertx.core.Handler
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.web.RoutingContext

/**
  * Created by leonardo on 7/19/17.
  */
case class ComponentRegisterHandler() extends Handler[RoutingContext] {

  override def handle(context: RoutingContext): Unit = {
    val response = Response(context)
    val component = Component(context.getBodyAsJson().getOrElse(Json.emptyObj()))
    val invalidFields = component.invalid
    if (invalidFields.nonEmpty) {
      response.badRequest(invalidFields)
      return
    }

    val componentJson = component.toJson
    context.vertx().eventBus().publish(Events.component.newOne, componentJson)
    response.accepted(componentJson)
  }

}
