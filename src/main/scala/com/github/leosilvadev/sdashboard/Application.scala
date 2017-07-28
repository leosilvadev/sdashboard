package com.github.leosilvadev.sdashboard

import com.github.leosilvadev.sdashboard.dashboard.DashboardServer
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
  val port = System.getenv().getOrDefault("PORT", "8080").toInt
  val options = DeploymentOptions().setConfig(Json.obj(("port", port)))

  vertx.deployVerticle(ScalaVerticle.nameForVerticle[DashboardServer], options, result => {
    logger.debug("Server running {}", result.result())
  })

}
