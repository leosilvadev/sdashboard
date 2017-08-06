package com.github.leosilvadev.sdashboard.auth

import com.github.leosilvadev.sdashboard.auth.handlers.AdminAuthenticationHandler
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.BodyHandler

/**
  * Created by leonardo on 8/5/17.
  */
case class AuthRouter(vertx: Vertx,
                      adminAuthenticationHandler: AdminAuthenticationHandler) {

  def routeV1(): Router = {
    val router = Router.router(vertx)
    router.post().handler(BodyHandler.create())
    router.post().handler(adminAuthenticationHandler)
    router
  }

}
