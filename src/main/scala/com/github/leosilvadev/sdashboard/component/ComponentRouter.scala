package com.github.leosilvadev.sdashboard.component

import com.github.leosilvadev.sdashboard.component.handlers.{ComponentListHandler, ComponentRegisterHandler, ComponentUnregisterHandler}
import com.github.leosilvadev.sdashboard.component.service.ComponentRepository
import com.github.leosilvadev.sdashboard.dashboard.domains.Dashboard
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.BodyHandler

/**
  * Created by leonardo on 7/29/17.
  */
case class ComponentRouter(vertx: Vertx, componentRepository: ComponentRepository, dashboard: Dashboard) {

  def routeV1(): Router = {
    val router = Router.router(vertx)
    router.post().handler(BodyHandler.create())
    router.post().handler(ComponentRegisterHandler(componentRepository, dashboard))
    router.get().handler(ComponentListHandler(componentRepository))
    router.delete("/:id").handler(ComponentUnregisterHandler(componentRepository, dashboard))
    router
  }

}
