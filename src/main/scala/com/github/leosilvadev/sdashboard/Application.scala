package com.github.leosilvadev.sdashboard

import com.github.leosilvadev.sdashboard.server.ServerVerticle
import com.typesafe.scalalogging.Logger
import io.vertx.lang.scala.json.Json
import io.vertx.lang.scala.{ScalaVerticle, VertxExecutionContext}
import io.vertx.scala.core.{DeploymentOptions, Vertx}

import scala.util.{Failure, Success}

/**
  * Created by leonardo on 7/11/17.
  */
object Application extends App {

  val logger = Logger(classOf[App])
  val vertx = Vertx.vertx()

  implicit val executionContext = VertxExecutionContext(vertx.getOrCreateContext())

  val config = Json.obj(
    ("dbName", System.getenv("DB_NAME")),
    ("dbUrl", System.getenv("DB_URL")),
    ("bootstrapFilePath", "dashboard.json")
  )
  val options = DeploymentOptions().setConfig(config)

  vertx.deployVerticleFuture(ScalaVerticle.nameForVerticle[ServerVerticle], options).onComplete {
    case Success(result) => logger.debug("Server running {}", result)

    case Failure(ex) =>
      logger.error(ex.getMessage, ex)
      vertx.close()
  }

}
