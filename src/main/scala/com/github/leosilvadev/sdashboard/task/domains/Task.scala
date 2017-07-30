package com.github.leosilvadev.sdashboard.task.domains

import com.github.leosilvadev.sdashboard.component.domains.{Component, Status}
import io.reactivex.ObservableEmitter
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.ext.web.client.WebClient

/**
  * Created by leonardo on 7/9/17.
  */
trait Task {

  def run(client: WebClient, emitter: ObservableEmitter[Status]): Unit

  def toJson: JsonObject

  def getFrequency: Long

  def getComponent: Component

}
