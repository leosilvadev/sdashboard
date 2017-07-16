package com.github.leosilvadev.sdashboard.task.exceptions

import io.vertx.core.buffer.Buffer
import io.vertx.scala.ext.web.client.HttpResponse

/**
  * Created by leonardo on 7/17/17.
  */
case class ResponseException(response: HttpResponse[Buffer]) extends RuntimeException {

  def status() = response.statusCode()

  def message() = response.bodyAsString("UTF-8").getOrElse("Empty response")

  override def toString: String = s"ResponseException(${status()}, ${message()})"
}
