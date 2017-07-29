package com.github.leosilvadev.sdashboard.server

import com.github.leosilvadev.sdashboard.Modules
import io.vertx.core.logging.LoggerFactory
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.mongo.MongoClient

import scala.collection.JavaConverters._
import scala.concurrent.Future

/**
  * Created by leonardo on 7/17/17.
  */
case class ServerVerticle() extends ScalaVerticle {

  val logger = LoggerFactory.getLogger(classOf[ServerVerticle])

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

      val modules = Modules(vertx, mongoClient)
      val server = vertx.createHttpServer()

      server.requestHandler(ServerRouter(vertx, modules).route().accept(_))

      modules.dashboard.builder.build()

      server.listen(8080)
      Future.successful(server)

    } catch {
      case ex: Exception => Future.failed(ex)

    }
  }

}
