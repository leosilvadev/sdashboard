package com.github.leosilvadev.sdashboard

import com.github.leosilvadev.sdashboard.component.service.ComponentChecker
import io.vertx.scala.core.Vertx
import com.github.leosilvadev.sdashboard.configuration.domains.Configuration

/**
  * Created by leonardo on 7/11/17.
  */
object Application extends App {

  val configFile = if (args.isEmpty) "config.json" else args(0)

  val vertx = Vertx.vertx()

  val json = vertx.fileSystem().readFileBlocking(configFile).toJsonObject

  val config = Configuration(json)

  val obs = config.components.flatMap(component => {
    Map(component.name -> ComponentChecker(vertx, component).start)
  })

  obs.foreach(c => {
    c._2.subscribe(result => println(s"${c._1}: ${result}"))
  })

  println(config)
}
