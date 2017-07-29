package com.github.leosilvadev.sdashboard.dashboard.handlers

import io.vertx.core.Handler
import io.vertx.core.logging.LoggerFactory
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.Vertx
import io.vertx.scala.core.eventbus.Message
import io.vertx.scala.ext.web.RoutingContext
import io.vertx.scala.ext.web.handler.sockjs.{SockJSHandler, SockJSHandlerOptions, SockJSSocket}

/**
  * Created by leonardo on 7/29/17.
  */
class SocketHandler(val vertx: Vertx) extends Handler[SockJSSocket] {

  val logger = LoggerFactory.getLogger(classOf[SocketHandler])

  override def handle(socket: SockJSSocket): Unit = {
    logger.info("New client connected, {}", socket.writeHandlerID())
    vertx.eventBus().consumer("components.status.update", (message: Message[JsonObject]) => {
      val status = message.body()
      socket.write(status.encode())
    })
  }

}

object SocketHandler {

  def apply(vertx: Vertx): Handler[RoutingContext] = {
    val options = SockJSHandlerOptions().setHeartbeatInterval(2000)
    val handler = SockJSHandler.create(vertx, options)
    handler.socketHandler(new SocketHandler(vertx))
    handler
  }

}
