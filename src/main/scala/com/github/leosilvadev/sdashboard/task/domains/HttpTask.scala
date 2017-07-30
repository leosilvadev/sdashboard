package com.github.leosilvadev.sdashboard.task.domains

import com.github.leosilvadev.sdashboard.component.domains.{Component, Status}
import io.reactivex.ObservableEmitter
import io.vertx.core.json.JsonObject
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.ext.web.client.WebClient

import scala.collection.JavaConverters._

/**
  * Created by leonardo on 7/9/17.
  */
case class HttpTask(component: Component, url: String, frequency: Long, headers: Map[String, String] = Map()) extends Task {

  def run(client: WebClient, emitter: ObservableEmitter[Status]): Unit = {
    val request = client.getAbs(url)
    headers.foreach((header) => request.putHeader(header._1, header._2))
    request.send(result => emitter.onNext(HttpTaskResponse(component, result).status))
  }

  def addHeader(key: String, value: String): HttpTask = {
    HttpTask(component, url, frequency, headers + (key -> value))
  }

  def toJson: JsonObject = {
    Json.obj(("url", url), ("frequency", frequency), ("headers", JsonObject.mapFrom(headers.asJava)))
  }

  def getFrequency: Long = frequency

  def getComponent: Component = component

}

object HttpTask {

  def apply(component: Component, json: JsonObject): HttpTask = {
    var task = new HttpTask(component, json.getString("url"), json.getLong("frequency"))
    json.getJsonObject("headers", Json.emptyObj()).forEach(entry => {
      task = task.addHeader(entry.getKey, entry.getValue.toString)
    })
    task
  }

  def apply(component: Component, url: String, frequency: Long): HttpTask = new HttpTask(component, url, frequency)

}