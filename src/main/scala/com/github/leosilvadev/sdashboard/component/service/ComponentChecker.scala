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
    vertx.eventBus().consumer(Events.component.registrationSucceeded, (message: Message[JsonObject]) => {
      val component = Component(message.body())
      merge(component.tasks.map(taskExecutor.execute).asJava).subscribe(
        result => vertx.eventBus().publish(Events.component.checkSuceeded, result.toJson),
        error => {
          logger.warn(error.getMessage, error)
          vertx.eventBus().publish(Events.component.checkFailed, Offline(component, ResponseException(error)).toJson)
        })
    })
  }

}
