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
case class ComponentRoutes(vertx: Vertx, componentRepository: ComponentRepository, dashboard: Dashboard) {

  def route(): Router = {
    val router = Router.router(vertx)
    router.post("/api/v1/components").handler(BodyHandler.create())
    router.get("/api/v1/components").handler(ComponentListHandler(componentRepository))
    router.post("/api/v1/components").handler(ComponentRegisterHandler(componentRepository, dashboard))
    router.delete("/api/v1/components/:id").handler(ComponentUnregisterHandler(componentRepository, dashboard))
    router
  }

}
