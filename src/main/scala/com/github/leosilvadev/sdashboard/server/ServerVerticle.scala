package com.github.leosilvadev.sdashboard.server

import com.github.leosilvadev.sdashboard.Modules
import com.github.leosilvadev.sdashboard.util.database.DatabaseMigrationRunner
import com.typesafe.scalalogging.Logger
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.mongo.MongoClient
import io.vertx.scala.ext.web.client.WebClient

import scala.collection.JavaConverters._
import scala.concurrent.Future

/**
  * Created by leonardo on 7/17/17.
  */
case class ServerVerticle() extends ScalaVerticle {

  val logger = Logger(classOf[ServerVerticle])

  override def startFuture(): Future[_] = {
    try {
      val hasConfigurations = config.fieldNames().containsAll(List("dbName", "dbUrl").asJava)

      if (!hasConfigurations) {
        return Future.failed(
          new RuntimeException("Missing configuration, please check. [dbName=required, dbUrl=required]")
        )
      }

      val dbName = config.getString("dbName")
      val dbUrl = config.getString("dbUrl")

      val mongoClient = MongoClient.createShared(vertx, Json.obj(("db_name", dbName), ("connection_string", dbUrl)))
      DatabaseMigrationRunner(mongoClient).migrate()

      val webClient = WebClient.create(vertx)
      val modules = Modules(vertx, mongoClient, webClient)
      val server = vertx.createHttpServer()

      val router = ServerRouter(vertx, modules).route()
      server.requestHandler(router.accept(_))

      modules.dashboard.builder.build()

      logger.info("# Configuring SDashboard routes...")
      router.getRoutes().foreach(route => {
        route.getPath().foreach(logger.info(_))
      })
      logger.info("# SDashboard routes configured.")
      server.listen(8080)
      Future.successful(server)

    } catch {
      case ex: Exception => Future.failed(ex)

    }
  }

}
