package com.github.leosilvadev.sdashboard.dashboard

import com.github.leosilvadev.sdashboard.auth.AuthModule
import com.github.leosilvadev.sdashboard.component.ComponentModule
import com.github.leosilvadev.sdashboard.dashboard.handlers.WSHandler
import com.github.leosilvadev.sdashboard.dashboard.services.DashboardBuilder
import io.vertx.scala.core.Vertx

/**
  * Created by leonardo on 7/29/17.
  */
case class DashboardModule(componentModule: ComponentModule, authModule: AuthModule)(implicit vertx: Vertx) {

  lazy val builder = DashboardBuilder(componentModule.repository, componentModule.checker)

  lazy val wsHandler = WSHandler(authModule.jWTAuth)

  lazy val router = DashboardRouter(wsHandler)

}
