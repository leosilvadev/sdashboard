package com.github.leosilvadev.sdashboard

import com.github.leosilvadev.sdashboard.dashboard.domains.{Configuration, Dashboard}
import io.vertx.scala.core.Vertx

/**
  * Created by leonardo on 7/11/17.
  */
object Application extends App {

  val configFile = if (args.isEmpty) "config.json" else args(0)

  val vertx = Vertx.vertx()

  val json = vertx.fileSystem().readFileBlocking(configFile).toJsonObject

  val config = Configuration(json)

  Dashboard(vertx, config).start().subscribe(status => {
    println(status)
  })

}
