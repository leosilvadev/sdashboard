package com.github.leosilvadev.sdashboard.dashboard

import com.github.leosilvadev.sdashboard.dashboard.domains.{Configuration, Dashboard}
import io.vertx.core.logging.LoggerFactory
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.sockjs.{SockJSHandler, SockJSHandlerOptions, SockJSSocket}

import scala.concurrent.Future


/**
  * Created by leonardo on 7/17/17.
  */
case class DashboardServer() extends ScalaVerticle {

  val logger = LoggerFactory.getLogger(classOf[DashboardServer])

  override def startFuture(): Future[_] = {
    try {
      val configFile = config.getString("configFile")
      val json = vertx.fileSystem().readFileBlocking(configFile).toJsonObject
      val dashboardPublisher = Dashboard(vertx, Configuration(json)).start().publish()
      dashboardPublisher.connect()

      val router = Router.router(vertx)
      val options = SockJSHandlerOptions().setHeartbeatInterval(2000)
      val handler = SockJSHandler.create(vertx, options)

      handler.socketHandler((socket: SockJSSocket) => {
        logger.info("New client connected, {}", socket.writeHandlerID())
        dashboardPublisher.subscribe(status => {
          socket.write(status.toJson.toBuffer)
        })
      })

      router.route("/dashboard/*").handler(handler)

      val server = vertx.createHttpServer()
      server.requestHandler(router.accept _)

      server.listen(8080)
      Future.successful()

    } catch {
      case ex: Exception => Future.failed(ex)

    }

  }

}
