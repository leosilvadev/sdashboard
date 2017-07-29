package com.github.leosilvadev.sdashboard.dashboard

import com.github.leosilvadev.sdashboard.dashboard.handlers.SocketHandler
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router

/**
  * Created by leonardo on 7/29/17.
  */
case class DashboardRoutes(vertx: Vertx) {

  def route(): Router = {
    val router = Router.router(vertx)
    router.route("/ws/dashboard/*").handler(SocketHandler(vertx))
    router
  }

}
