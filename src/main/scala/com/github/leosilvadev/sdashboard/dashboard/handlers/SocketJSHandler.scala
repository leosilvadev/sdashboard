package com.github.leosilvadev.sdashboard.dashboard.handlers

import com.typesafe.scalalogging.Logger
import io.vertx.core.Handler
import io.vertx.lang.scala.VertxExecutionContext
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.core.Vertx
import io.vertx.scala.core.eventbus.Message
import io.vertx.scala.ext.auth.jwt.JWTAuth
import io.vertx.scala.ext.web.handler.sockjs.SockJSSocket

import scala.util.{Failure, Success}

/**
  * Created by leonardo on 7/29/17.
  */
case class SocketJSHandler(vertx: Vertx, jWTAuth: JWTAuth) extends Handler[SockJSSocket] {

  protected implicit val executionContext = VertxExecutionContext(vertx.getOrCreateContext())

  val logger = Logger(classOf[SocketJSHandler])

  override def handle(socket: SockJSSocket): Unit = {
    val uri = socket.uri()
    if (uri.contains("token=")) {
      val token = uri.split("token=")(1)
      jWTAuth.authenticateFuture(Json.obj(("jwt", token))).onComplete {
        case Success(_) =>
          logger.info("New client connected, {}", socket.writeHandlerID())
          vertx.eventBus().consumer("components.status", (message: Message[JsonObject]) => {
            val status = message.body()
            socket.write(status.encode())
          })

        case Failure(ex) =>
          logger.warn("Not possible to authorize socket access", ex)
          socket.end(Json.obj(("socket_error", "access_denied")).toBuffer)
      }
    } else {
      socket.end(Json.obj(("socket_error", "access_denied")).toBuffer)
    }
  }

}
