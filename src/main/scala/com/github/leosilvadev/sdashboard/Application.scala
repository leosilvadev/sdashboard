package com.github.leosilvadev.sdashboard

import com.github.leosilvadev.sdashboard.server.ServerVerticle
import com.typesafe.scalalogging.Logger
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.{DeploymentOptions, Vertx}

/**
  * Created by leonardo on 7/11/17.
  */
object Application extends App {

  val logger = Logger(classOf[App])
  val vertx = Vertx.vertx()

  val config = Json.obj(("dbName", System.getenv("DB_NAME")), ("dbUrl", System.getenv("DB_URL")))
  val options = DeploymentOptions().setConfig(config)

  vertx.deployVerticle(ScalaVerticle.nameForVerticle[ServerVerticle], options, result => {
    if (result.succeeded()) {
      logger.debug("Server running {}", result)
    } else {
      val ex = result.cause()
      logger.error(ex.getMessage, ex)
      ex.printStackTrace()
      vertx.close()
    }
  })

}
