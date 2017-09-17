package com.github.leosilvadev.sdashboard.dashboard

import com.github.leosilvadev.sdashboard.auth.AuthModule
import com.github.leosilvadev.sdashboard.component.ComponentModule
import com.github.leosilvadev.sdashboard.dashboard.handlers.WSHandler
import com.github.leosilvadev.sdashboard.dashboard.services.{DashboardBuilder, DashboardEventListener, DashboardRepository}
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.mongo.MongoClient

/**
  * Created by leonardo on 7/29/17.
  */
case class DashboardModule(mongoClient: MongoClient, componentModule: ComponentModule, authModule: AuthModule)(implicit vertx: Vertx) {

  lazy val repository = DashboardRepository(mongoClient)

  lazy val builder = DashboardBuilder(repository)

  lazy val wsHandler = WSHandler(authModule.jWTAuth)

  lazy val router = DashboardRouter(wsHandler, repository, componentModule.checker)

  DashboardEventListener(repository).start()

}
