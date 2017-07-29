package com.github.leosilvadev.sdashboard.dashboard

import com.github.leosilvadev.sdashboard.component.ComponentModule
import com.github.leosilvadev.sdashboard.dashboard.handlers.WSHandler
import com.github.leosilvadev.sdashboard.dashboard.services.DashboardBuilder
import io.vertx.scala.core.Vertx

/**
  * Created by leonardo on 7/29/17.
  */
case class DashboardModule(vertx: Vertx, componentModule: ComponentModule) {

  lazy val builder = DashboardBuilder(vertx, componentModule.repository, componentModule.checker)

  lazy val wsHandler = WSHandler(vertx)

  lazy val router = DashboardRouter(vertx, wsHandler)

}
