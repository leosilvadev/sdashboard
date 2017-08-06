package com.github.leosilvadev.sdashboard.auth.handlers

import com.github.leosilvadev.sdashboard.auth.util.Auth
import com.github.leosilvadev.sdashboard.util.Response
import io.vertx.core.Handler
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.auth.AuthProvider
import io.vertx.scala.ext.auth.jwt.{JWTAuth, JWTOptions}
import io.vertx.scala.ext.web.RoutingContext

/**
  * Created by leonardo on 8/5/17.
  */
case class AdminAuthenticationHandler(authProvider: AuthProvider, jWTAuth: JWTAuth) extends Handler[RoutingContext] {

  def handle(context: RoutingContext): Unit = {
    val response = Response(context)

    Auth.adminFromBody(context) match {
      case Some(admin) =>
        authProvider.authenticate(admin.toJson, result => {
          if (result.succeeded()) {
            val token = jWTAuth.generateToken(Json.obj(("username", admin.email)), JWTOptions().setExpiresInMinutes(60))
            response.ok(Json.obj(("token", token)))
          } else {
            response.unauthorized()
          }
        })

      case None => response.unauthorized()
    }
  }

}
