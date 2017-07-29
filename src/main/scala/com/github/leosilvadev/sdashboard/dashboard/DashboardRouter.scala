package com.github.leosilvadev.sdashboard.dashboard

import com.github.leosilvadev.sdashboard.dashboard.handlers.WSHandler
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router

/**
  * Created by leonardo on 7/29/17.
  */
case class DashboardRouter(vertx: Vertx, wsHandler: WSHandler) {

  def routeV1(): Router = {
    val router = Router.router(vertx)
    router.route("/*").handler(wsHandler.sockJsHandler())
    router
  }

}
