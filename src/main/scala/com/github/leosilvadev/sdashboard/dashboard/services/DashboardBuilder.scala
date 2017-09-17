package com.github.leosilvadev.sdashboard.dashboard.services

import com.github.leosilvadev.sdashboard.component.domains.Component
import com.github.leosilvadev.sdashboard.dashboard.domains.Dashboard
import com.typesafe.scalalogging.Logger
import io.vertx.lang.scala.json.{JsonArray, JsonObject}
import io.vertx.scala.core.Vertx
import io.vertx.scala.core.eventbus.Message

import scala.collection.mutable

/**
  * Created by leonardo on 7/29/17.
  */
case class DashboardBuilder(dashboardRepository: DashboardRepository) {

  val logger = Logger(classOf[DashboardBuilder])

  def build(dashboardsBootstrap: JsonArray)(implicit vertx: Vertx): List[Dashboard] = {
    val dashboards = mutable.Buffer[Dashboard]()
    dashboardsBootstrap.forEach(item => {
      val dashboard = Dashboard(item.asInstanceOf[JsonObject])

      vertx.eventBus().consumer(s"dashboards.${dashboard.id}.components.register", (message: Message[JsonObject]) => {
        dashboardRepository.update(dashboard.addComponent(Component(message.body())))
      })

      vertx.eventBus().consumer(s"dashboards.${dashboard.id}.components.unregister", (message: Message[String]) => {
        dashboardRepository.update(dashboard.removeComponent(message.body()))
      })

      dashboards += dashboard
    })
    dashboards.toList
  }

}
