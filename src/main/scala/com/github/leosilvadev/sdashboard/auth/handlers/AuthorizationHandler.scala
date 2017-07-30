package com.github.leosilvadev.sdashboard.auth.handlers

import java.util.Base64

import com.github.leosilvadev.sdashboard.auth.domains.Admin
import io.vertx.core.Handler
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.auth.AuthProvider
import io.vertx.scala.ext.web.RoutingContext

/**
  * Created by leonardo on 7/30/17.
  */
case class AuthorizationHandler(vertx: Vertx, authProvider: AuthProvider) extends Handler[RoutingContext] {

  override def handle(context: RoutingContext): Unit = {
    val headers = context.request().headers()
    if (headers.contains("Authorization")) {
      val authorization = headers.get("Authorization").get.replace("Basic ", "")
      val data = new String(Base64.getDecoder.decode(authorization.getBytes("UTF-8")), "UTF-8").split(":")
      authProvider.authenticate(Admin(data(0), data(1)).toJson, result => {
        if (result.succeeded()) {
          context.next()
        } else {
          context.fail(401)
        }
      })
    } else {
      context.fail(401)

    }
  }

}
