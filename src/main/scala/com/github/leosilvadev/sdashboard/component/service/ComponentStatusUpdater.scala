package com.github.leosilvadev.sdashboard.component.service

import com.github.leosilvadev.sdashboard.Events
import com.github.leosilvadev.sdashboard.component.domains.{ComponentSnapshot, Status}
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.Vertx
import io.vertx.scala.core.eventbus.{Message, MessageConsumer}

import scala.collection.mutable

/**
  * Created by leonardo on 8/12/17.
  */
case class ComponentStatusUpdater(repository: ComponentStatusRepository)(implicit vertx: Vertx) {

  private val consumers: mutable.MutableList[MessageConsumer[JsonObject]] = mutable.MutableList.empty

  def start(): Unit = {
    consumers += vertx.eventBus().consumer(Events.component.checkSuceeded, persist)
    consumers += vertx.eventBus().consumer(Events.component.checkFailed, persist)
  }

  def stop(): Unit = {
    consumers.foreach(_.unregister)
  }

  private def persist(message: Message[JsonObject]): Unit = {
    Status.of(message.body()).foreach(status => {
      ComponentSnapshot(status).foreach(repository.createSnapshot)
    })
  }
}
