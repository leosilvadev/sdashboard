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
    router.route("/api/*").handler(modules.auth.authorizationHandler)
    router.mountSubRouter("/", StaticRouter(vertx).route())
    router.mountSubRouter("/api/v1/components", modules.component.router.routeV1())
    router.mountSubRouter("/ws/v1/dashboard", modules.dashboard.router.routeV1())
    router.route().handler(CorsHandler.create("*").allowedHeader("Content-Type"))
    router
  }

}
