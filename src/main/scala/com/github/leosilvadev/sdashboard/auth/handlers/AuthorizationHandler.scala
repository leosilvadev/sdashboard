package com.github.leosilvadev.sdashboard.auth.handlers

import java.util.Base64

import com.github.leosilvadev.sdashboard.auth.domains.Admin
import io.vertx.core.Handler
import io.vertx.lang.scala.VertxExecutionContext
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.auth.AuthProvider
import io.vertx.scala.ext.web.RoutingContext

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Created by leonardo on 7/30/17.
  */
case class AuthorizationHandler(vertx: Vertx, authProvider: AuthProvider) extends Handler[RoutingContext] {

  implicit val ec: ExecutionContext = VertxExecutionContext(vertx.getOrCreateContext())

  override def handle(context: RoutingContext): Unit = {
    val headers = context.request().headers()
    if (headers.contains("Authorization")) {
      val authorization = headers.get("Authorization").get.replace("Basic ", "")
      val data = new String(Base64.getDecoder.decode(authorization.getBytes("UTF-8")), "UTF-8").split(":")
      authProvider.authenticateFuture(Admin(data(0), data(1)).toJson).onComplete {
        case Success(result) => {
          println(result)
          context.next()
        }
        case Failure(ex) => {
          ex.printStackTrace()
          context.fail(401)
        }
      }
    } else {
      context.fail(401)

    }
  }

}
