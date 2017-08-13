package com.github.leosilvadev.sdashboard.static

import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.StaticHandler

/**
  * Created by leonardo on 7/29/17.
  */
case class StaticRouter(vertx: Vertx) {

  def route(): Router = {
    val router = Router.router(vertx)
    router.get("/").handler(StaticHandler.create("src/main/resources/ui/build/").setIndexPage("index.html"))
    router.get("/assets/*").handler(StaticHandler.create("src/main/resources/ui/build/assets"))
    router
  }

}
