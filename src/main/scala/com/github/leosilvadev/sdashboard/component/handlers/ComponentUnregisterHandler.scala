package com.github.leosilvadev.sdashboard.component.handlers

import com.github.leosilvadev.sdashboard.component.service.ComponentRepository
import com.github.leosilvadev.sdashboard.util.Response
import io.vertx.core.Handler
import io.vertx.scala.ext.web.RoutingContext

/**
  * Created by leonardo on 7/19/17.
  */
case class ComponentUnregisterHandler(repository: ComponentRepository) extends Handler[RoutingContext] {

  override def handle(context: RoutingContext): Unit = {
    val response = Response(context)
    val maybeId = context.request().params().get("id")
    if (maybeId.isEmpty) {
      response.badRequest(List("id"))
      return
    }

    repository.unregister(maybeId.get)
      .doOnSuccess(componentId => {
        context.vertx().eventBus().send("components.unregister", componentId)
      })
      .subscribe(id => response.noContent(), response.internalError(_))
  }

}
