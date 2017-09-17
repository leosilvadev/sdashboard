package com.github.leosilvadev.sdashboard.server

import com.github.leosilvadev.sdashboard.Modules
import com.github.leosilvadev.sdashboard.static.StaticRouter
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.CorsHandler

/**
  * Created by leonardo on 7/29/17.
  */
case class ServerRouter(vertx: Vertx, modules: Modules) {

  def route(): Router = {
    val router = Router.router(vertx)
    router.mountSubRouter("/", StaticRouter(vertx).route())
    router.mountSubRouter("/api/v1/auth", modules.auth.router.routeV1())

    router.route("/api/v1/dashboards/*").handler(modules.auth.authorizationMiddleware)

    router.mountSubRouter("/ws/v1/dashboard", modules.dashboard.router.routeWebsocketV1())
    router.route().handler(CorsHandler.create("*").allowedHeader("Content-Type"))
    router
  }

}
