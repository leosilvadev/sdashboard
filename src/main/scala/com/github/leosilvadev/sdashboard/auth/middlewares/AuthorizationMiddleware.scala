package com.github.leosilvadev.sdashboard.auth.middlewares

import com.github.leosilvadev.sdashboard.auth.util.Auth
import io.vertx.core.Handler
import io.vertx.lang.scala.VertxExecutionContext
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.auth.jwt.JWTAuth
import io.vertx.scala.ext.web.RoutingContext

import scala.util.{Failure, Success}

/**
  * Created by leonardo on 7/30/17.
  */
case class AuthorizationMiddleware(vertx: Vertx, jWTAuth: JWTAuth) extends Handler[RoutingContext] {

  override def handle(context: RoutingContext): Unit = {
    Auth.tokenFromHeader(context) match {
      case Some(token) =>
        jWTAuth.authenticate(Json.obj(("jwt", token)), result => {
          if (result.succeeded()) {
            context.next()
          } else {
            context.fail(401)
          }
        })
      case None => context.fail(401)
    }
  }

}
