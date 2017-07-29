package com.github.leosilvadev.sdashboard.dashboard.handlers

import io.vertx.core.Handler
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.RoutingContext
import io.vertx.scala.ext.web.handler.sockjs.{SockJSHandler, SockJSHandlerOptions}

/**
  * Created by leonardo on 7/29/17.
  */
case class WSHandler(vertx: Vertx) {

  def sockJsHandler(): Handler[RoutingContext] = {
    val options = SockJSHandlerOptions().setHeartbeatInterval(2000)
    val handler = SockJSHandler.create(vertx, options)
    handler.socketHandler(SocketJSHandler(vertx))
    handler
  }

}
