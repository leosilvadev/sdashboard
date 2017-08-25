package com.github.leosilvadev.sdashboard.component.service

import com.github.leosilvadev.sdashboard.component.domains.{ComponentSnapshot, Status}
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.Vertx
import io.vertx.scala.core.eventbus.{Message, MessageConsumer}

import scala.collection.mutable

/**
  * Created by leonardo on 8/12/17.
  */
case class ComponentStatusUpdater(repository: ComponentRepository)(implicit vertx: Vertx) {

  val consumers: mutable.MutableList[MessageConsumer[JsonObject]] = mutable.MutableList.empty

  def start(): Unit = {
    consumers += vertx.eventBus().consumer("components.status", (message: Message[JsonObject]) => {
      Status.of(message.body()).foreach(status => {
        ComponentSnapshot(status).foreach(repository.createSnapshot)
      })
    })
  }

  def stop(): Unit = {
    consumers.foreach(_.unregister)
  }

}
