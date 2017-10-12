package com.github.leosilvadev.sdashboard.component.service

import com.github.leosilvadev.sdashboard.Events
import com.github.leosilvadev.sdashboard.component.domains.Component
import com.github.leosilvadev.sdashboard.component.domains.Status.Offline
import com.github.leosilvadev.sdashboard.dashboard.services.DashboardRepository
import com.github.leosilvadev.sdashboard.task.exceptions.ResponseException
import com.github.leosilvadev.sdashboard.task.services.TaskExecutor
import com.typesafe.scalalogging.Logger
import io.reactivex.Observable._
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.Vertx
import io.vertx.scala.core.eventbus.Message

import scala.collection.JavaConverters._

/**
  * Created by leonardo on 7/11/17.
  */
case class ComponentChecker(taskExecutor: TaskExecutor)(implicit vertx: Vertx) {

  val logger = Logger(classOf[DashboardRepository])

  def start(): Unit = {
    logger.info("Starting Component checker")
    vertx.eventBus().consumer(Events.component.registrationSucceeded, (message: Message[JsonObject]) => {
      check(Component(message.body()))
    })

    vertx.eventBus().consumer(Events.component.check, (message: Message[JsonObject]) => {
      check(Component(message.body()))
    })
    logger.info("Component checker started, listening to {} and {}", Events.component.registrationSucceeded, Events.component.check)
  }

  def check(component: Component): Unit = {
    merge(component.tasks.map(taskExecutor.execute).asJava).subscribe(
      result => {
        logger.info("Result of component execution: {}", result)
        vertx.eventBus().publish(Events.component.checkSuceeded, result.toJson)
      },
      error => {
        logger.warn(error.getMessage, error)
        vertx.eventBus().publish(Events.component.checkFailed, Offline(component, ResponseException(error)).toJson)
      })
  }

}
