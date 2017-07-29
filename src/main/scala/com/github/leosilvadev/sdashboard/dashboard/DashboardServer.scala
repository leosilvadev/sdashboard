package com.github.leosilvadev.sdashboard.dashboard

import com.github.leosilvadev.sdashboard.component.ComponentRouter
import com.github.leosilvadev.sdashboard.component.service.ComponentRepository
import com.github.leosilvadev.sdashboard.dashboard.services.DashboardBuilder
import com.github.leosilvadev.sdashboard.task.services.TaskExecutor
import io.vertx.core.logging.LoggerFactory
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.mongo.MongoClient
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.{CorsHandler, StaticHandler}

import scala.collection.JavaConverters._
import scala.concurrent.Future

/**
  * Created by leonardo on 7/17/17.
  */
case class DashboardServer() extends ScalaVerticle {

  val logger = LoggerFactory.getLogger(classOf[DashboardServer])

  override def startFuture(): Future[_] = {
    try {
      val hasConfigurations = config.fieldNames().containsAll(List("port", "dbName", "dbUrl").asJava)

      if (!hasConfigurations) {
        return Future.failed(
          new RuntimeException("Missing configuration, please check. [port=required, dbName=required, sdashboard=requred]")
        )
      }

      val dbName = config.getString("dbName")
      val dbUrl = config.getString("dbUrl")
      val port = config.getInteger("port", 8080)

      val router = Router.router(vertx)
      val mongoClient = MongoClient.createShared(vertx, Json.obj(("db_name", dbName), ("connection_string", dbUrl)))
      val componentRepository = ComponentRepository(mongoClient)
      val taskExecutor = TaskExecutor(vertx)
      val dashboard = DashboardBuilder(vertx, componentRepository, taskExecutor).build()

      router.mountSubRouter("/api/v1/components", ComponentRouter(vertx, componentRepository, dashboard).routeV1())
      router.mountSubRouter("/ws/v1/dashboard", DashboardRouter(vertx).routeV1())

      router.route("/").handler(StaticHandler.create("src/main/resources/").setIndexPage("index.html"))
      router.route("/assets/*").handler(StaticHandler.create("src/main/resources/assets"))

      router.route().handler(CorsHandler.create("*").allowedHeader("Content-Type"))

      val server = vertx.createHttpServer()
      server.requestHandler(router.accept _)

      server.listen(port)

      Future.successful(server)

    } catch {
      case ex: Exception => Future.failed(ex)

    }

  }

}
