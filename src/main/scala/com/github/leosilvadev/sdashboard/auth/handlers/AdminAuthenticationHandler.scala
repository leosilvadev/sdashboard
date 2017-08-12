package com.github.leosilvadev.sdashboard.auth.handlers

import com.github.leosilvadev.sdashboard.auth.util.Auth
import com.github.leosilvadev.sdashboard.util.Response
import com.typesafe.scalalogging.Logger
import io.vertx.core.Handler
import io.vertx.lang.scala.VertxExecutionContext
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.auth.AuthProvider
import io.vertx.scala.ext.auth.jwt.{JWTAuth, JWTOptions}
import io.vertx.scala.ext.web.RoutingContext

import scala.util.{Failure, Success}

/**
  * Created by leonardo on 8/5/17.
  */
case class AdminAuthenticationHandler(vertx: Vertx, authProvider: AuthProvider, jWTAuth: JWTAuth) extends Handler[RoutingContext] {

  protected implicit val executionContext = VertxExecutionContext(vertx.getOrCreateContext())

  val logger = Logger(classOf[AdminAuthenticationHandler])

  def handle(context: RoutingContext): Unit = {
    val response = Response(context)

    Auth.adminFromBody(context) match {
      case Some(admin) =>
        authProvider.authenticateFuture(admin.toJson).onComplete {
          case Success(_) =>
            val token = jWTAuth.generateToken(Json.obj(("username", admin.email)), JWTOptions().setExpiresInMinutes(60))
            response.ok(Json.obj(("token", token)))

          case Failure(ex) =>
            logger.warn("Not possible to authenticate the user", ex)
            response.unauthorized()
        }

      case None => response.unauthorized()
    }
  }

}
