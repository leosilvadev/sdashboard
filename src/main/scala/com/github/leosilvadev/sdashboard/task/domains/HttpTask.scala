package com.github.leosilvadev.sdashboard.task.domains

import com.github.leosilvadev.sdashboard.component.domains.{Component, Status}
import com.github.leosilvadev.sdashboard.task.exceptions.ResponseException
import io.reactivex.Observable
import io.vertx.core.json.JsonObject
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.client.WebClient

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * Created by leonardo on 7/9/17.
  */
case class HttpTask(component: Component, url: String, frequency: Long, headers: Map[String, String] = Map()) extends Task {

  def start(vertx: Vertx): Observable[Status] = {
    Observable.create[Status](emitter => {
      vertx.setPeriodic(frequency, n => {
        val client = WebClient.create(vertx).getAbs(url)
        headers.foreach((header) => client.putHeader(header._1, header._2))
        client.sendFuture().onComplete {
          case Success(result) if result.statusCode() >= 200 && result.statusCode() < 400 =>
            try {
              val json = result.bodyAsJsonObject().getOrElse(Json.emptyObj())
              emitter.onNext(Status.Online(component, json))
            } catch {
              case _: Exception => emitter.onNext(Status.Offline(component, ResponseException(500, "Status endpoint returned a non-json response")))
            }

          case Success(result) =>
            emitter.onNext(Status.Offline(component, ResponseException(result)))

          case Failure(ex) =>
            emitter.onNext(Status.Offline(component, ResponseException(ex)))
        }
      })
    })
  }

  def addHeader(key: String, value: String) = {
    HttpTask(component, url, frequency, headers + (key -> value))
  }

  def toJson: JsonObject = {
    Json.obj(("url", url), ("frequency", frequency), ("headers", JsonObject.mapFrom(headers.asJava)))
  }

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