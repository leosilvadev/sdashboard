package com.github.leosilvadev.sdashboard.dashboard

import com.github.leosilvadev.sdashboard.component.handlers.{ComponentListHandler, ComponentRegisterHandler, ComponentUnregisterHandler}
import com.github.leosilvadev.sdashboard.component.service.ComponentRepository
import com.github.leosilvadev.sdashboard.dashboard.domains.Dashboard
import io.vertx.core.logging.LoggerFactory
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.core.eventbus.Message
import io.vertx.scala.ext.mongo.MongoClient
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.BodyHandler
import io.vertx.scala.ext.web.handler.sockjs.{SockJSHandler, SockJSHandlerOptions, SockJSSocket}

import scala.concurrent.Future


/**
  * Created by leonardo on 7/17/17.
  */
case class DashboardServer() extends ScalaVerticle {

  val logger = LoggerFactory.getLogger(classOf[DashboardServer])

  override def startFuture(): Future[_] = {
    try {
      val router = Router.router(vertx)
      val options = SockJSHandlerOptions().setHeartbeatInterval(2000)
      val handler = SockJSHandler.create(vertx, options)

      handler.socketHandler((socket: SockJSSocket) => {
        logger.info("New client connected, {}", socket.writeHandlerID())
        vertx.eventBus().consumer("components.status.update", (message: Message[JsonObject]) => {
          val status = message.body()
          socket.write(status.encode())
        })
      })

      val mongoClient = MongoClient.createShared(vertx, Json.obj(("db_name", "sdashboard")))
      val componentRepository = ComponentRepository(mongoClient)
      val dashboard = Dashboard(vertx)

      router.post().handler(BodyHandler.create())
      router.get("/api/v1/components").handler(ComponentListHandler(componentRepository))
      router.post("/api/v1/components").handler(ComponentRegisterHandler(componentRepository, dashboard))
      router.delete("/api/v1/components/:id").handler(ComponentUnregisterHandler(componentRepository, dashboard))
      router.route("/ws/dashboard/*").handler(handler)

      val server = vertx.createHttpServer()
      server.requestHandler(router.accept _)

      server.listen(8080)
      Future.successful()

    } catch {
      case ex: Exception => Future.failed(ex)

    }

  }

}
