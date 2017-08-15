package com.github.leosilvadev.sdashboard.dashboard.services

import com.github.leosilvadev.sdashboard.component.domains.Component
import com.github.leosilvadev.sdashboard.component.service.{ComponentChecker, ComponentRepository}
import com.github.leosilvadev.sdashboard.dashboard.domains.Dashboard
import com.typesafe.scalalogging.Logger
import io.vertx.lang.scala.json.{JsonArray, JsonObject}
import io.vertx.scala.core.Vertx
import io.vertx.scala.core.eventbus.Message

/**
  * Created by leonardo on 7/29/17.
  */
case class DashboardBuilder(vertx: Vertx, componentRepository: ComponentRepository, componentChecker: ComponentChecker) {

  val logger = Logger(classOf[DashboardBuilder])

  def build(componentsBootstrap: JsonArray): Dashboard = {
    val dashboard = Dashboard(vertx, componentChecker)

    componentsBootstrap.forEach(json => {
      dashboard.register(Component(json.asInstanceOf[JsonObject]))
    })

    componentRepository.list().subscribe(dashboard.reloadComponents(_), ex => logger.error(ex.getMessage, ex))

    vertx.eventBus().consumer("components.register", (message: Message[JsonObject]) => {
      dashboard.register(Component(message.body()))
    })

    vertx.eventBus().consumer("components.unregister", (message: Message[String]) => {
      dashboard.unregister(message.body())
    })

    dashboard
  }

}
