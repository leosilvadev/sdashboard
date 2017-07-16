package com.github.leosilvadev.sdashboard.task.domains

import java.util.concurrent.TimeUnit

import com.github.leosilvadev.sdashboard.component.domains.Status
import com.github.leosilvadev.sdashboard.task.exceptions.ResponseException
import io.reactivex.Observable
import io.vertx.core.json.JsonObject
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.client.WebClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * Created by leonardo on 7/9/17.
  */
case class HttpTask(url: String, frequency: Long, headers: Map[String, String] = Map()) extends Task {

  def start(vertx: Vertx): Observable[Status] = {
    val client = WebClient.create(vertx).get(url)
    headers.foreach((header) => client.putHeader(header._1, header._2))
    Observable.create[Status](emitter => {
      vertx.setPeriodic(frequency, n => {
        client.sendFuture().onComplete {
          case Success(result) if result.statusCode() >= 200 && result.statusCode() < 400 =>
            emitter.onNext(Status.Online(result.bodyAsJsonObject().getOrElse(Json.emptyObj())))

          case Success(result) =>
            emitter.onNext(Status.Offline(ResponseException(result)))

          case Failure(ex) =>
            emitter.onNext(Status.Offline(ex))
        }
      })
    })
  }

  def addHeader(key: String, value: String) = {
    HttpTask(url, frequency, headers + (key -> value))
  }

  def toJson: JsonObject = {
    Json.obj(("url", url), ("frequency", frequency), ("headers", JsonObject.mapFrom(headers)))
  }

}

object HttpTask {

  def apply(json: JsonObject): HttpTask = {
    var task = new HttpTask(json.getString("url"), json.getLong("frequency"))
    json.getJsonObject("headers").forEach(entry => {
      task = task.addHeader(entry.getKey, entry.getValue.toString)
    })
    task
  }

}