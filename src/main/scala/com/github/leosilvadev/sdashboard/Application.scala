package com.github.leosilvadev.sdashboard

import com.github.leosilvadev.sdashboard.dashboard.DashboardServer
import com.typesafe.scalalogging.Logger
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.Vertx

/**
  * Created by leonardo on 7/11/17.
  */
object Application extends App {

  val logger = Logger(classOf[App])
  val configFile = if (args.isEmpty) "config.json" else args(0)
  val vertx = Vertx.vertx()

  vertx.deployVerticle(ScalaVerticle.nameForVerticle[DashboardServer], result => {
    logger.debug("Server running {}", result.result())
  })

}
