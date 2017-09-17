package com.github.leosilvadev.sdashboard.dashboard

import com.github.leosilvadev.sdashboard.component.service.ComponentChecker
import com.github.leosilvadev.sdashboard.dashboard.domains.Dashboard
import com.github.leosilvadev.sdashboard.dashboard.handlers.WSHandler
import com.github.leosilvadev.sdashboard.dashboard.services.DashboardRepository
import com.github.leosilvadev.sdashboard.util.Response
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.BodyHandler

/**
  * Created by leonardo on 7/29/17.
  */
case class DashboardRouter(wsHandler: WSHandler, dashboardRepository: DashboardRepository, componentChecker: ComponentChecker)(implicit vertx: Vertx) {

  def routeWebsocketV1(): Router = {
    val router = Router.router(vertx)
    router.route("/*").handler(wsHandler.sockJsHandler())
    router
  }

  def routeApiV1(): Router = {
    val router = Router.router(vertx)
    router.post().handler(BodyHandler.create())
    router.post("/").handler(context => {
      val response = Response(context)
      val dashboard = Dashboard(context.getBodyAsJson().getOrElse(Json.emptyObj()))
      dashboardRepository.register(dashboard)
      response.created()
    })
    router
  }

}
