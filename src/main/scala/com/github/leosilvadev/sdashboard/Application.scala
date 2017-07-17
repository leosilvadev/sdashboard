package com.github.leosilvadev.sdashboard

import com.github.leosilvadev.sdashboard.dashboard.DashboardServer
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.{DeploymentOptions, Vertx}

/**
  * Created by leonardo on 7/11/17.
  */
object Application extends App {

  val configFile = if (args.isEmpty) "config.json" else args(0)

  val vertx = Vertx.vertx()

  val deploymentOptions = DeploymentOptions().setConfig(Json.obj(("configFile", configFile)))

  vertx.deployVerticle(ScalaVerticle.nameForVerticle[DashboardServer], deploymentOptions, result => {
    println(result.succeeded())
  })

}
