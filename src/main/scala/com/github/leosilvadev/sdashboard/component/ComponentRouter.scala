package com.github.leosilvadev.sdashboard.component

import com.github.leosilvadev.sdashboard.component.handlers.{ComponentListHandler, ComponentRegisterHandler, ComponentUnregisterHandler}
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.BodyHandler

/**
  * Created by leonardo on 7/29/17.
  */
case class ComponentRouter(vertx: Vertx,
                           componentListHandler: ComponentListHandler,
                           componentRegisterHandler: ComponentRegisterHandler,
                           componentUnregisterHandler: ComponentUnregisterHandler) {

  def routeV1(): Router = {
    val router = Router.router(vertx)
    router.post().handler(BodyHandler.create())
    router.post().handler(componentRegisterHandler)
    router.get().handler(componentListHandler)
    router.delete("/:id").handler(componentUnregisterHandler)
    router
  }

}
