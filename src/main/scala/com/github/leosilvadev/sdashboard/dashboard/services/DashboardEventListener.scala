package com.github.leosilvadev.sdashboard.dashboard.services

import com.github.leosilvadev.sdashboard.Events
import com.github.leosilvadev.sdashboard.component.domains.Component
import com.github.leosilvadev.sdashboard.dashboard.domains.Dashboard
import com.typesafe.scalalogging.Logger
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.core.Vertx
import io.vertx.scala.core.eventbus.Message

/**
  * Created by leonardo on 9/17/17.
  */
case class DashboardEventListener(dashboardRepository: DashboardRepository)(implicit vertx: Vertx) {

  val logger = Logger(classOf[DashboardEventListener])

  def start(): Unit = {
    vertx.eventBus().consumer(Events.component.newOne, (message: Message[JsonObject]) => {
      val component = Component(message.body())
      dashboardRepository.findOneById(component.dashboardId)
        .map(_.addComponent(component))
        .flatMap[Dashboard](dashboardRepository.update)
        .subscribe(
          dashboard => vertx.eventBus().publish(Events.component.registrationSucceeded, dashboard.toJson),
          error => {
            logger.warn("Error on registering new Component", error)
            vertx.eventBus().publish(Events.component.registrationFailed, Json.obj(("error", error.getMessage)))
          }
        )
    })
  }

}
