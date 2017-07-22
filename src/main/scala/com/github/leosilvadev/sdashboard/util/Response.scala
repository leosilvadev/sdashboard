package com.github.leosilvadev.sdashboard.util

import com.typesafe.scalalogging.Logger
import io.vertx.lang.scala.json.{Json, JsonArray, JsonObject}
import io.vertx.scala.ext.web.RoutingContext

/**
  * Created by leonardo on 7/19/17.
  */
case class Response(context: RoutingContext) {

  val logger = Logger(classOf[Response])

  def created(id: String): Unit = {
    respond(201, Json.obj(("_id", id)))
  }

  def created(json: JsonObject = Json.emptyObj()): Unit = {
    respond(201, json)
  }

  def noContent(): Unit = {
    respond(204)
  }

  def badRequest(fields: List[String]): Unit = {
    respond(400, Json.obj(("fields", fields)))
  }

  def internalError(ex: Throwable): Unit = {
    logger.error(ex.getMessage, ex)
    respond(500, Json.obj(("message", ex.getMessage)))
  }

  def ok(json: JsonObject = Json.emptyObj()): Unit = {
    respond(200, json)
  }

  def ok(json: JsonArray): Unit = {
    respond(200, json)
  }

  def respond(status: Int, json: JsonObject = Json.emptyObj()): Unit = {
    context.response()
      .setStatusCode(status)
      .setChunked(true)
      .write(json.encode())
      .end()
  }

  def respond(status: Int, json: JsonArray): Unit = {
    context.response()
      .setStatusCode(status)
      .setChunked(true)
      .write(json.encode())
      .end()
  }

}
