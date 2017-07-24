package com.github.leosilvadev.sdashboard.task.exceptions

import io.vertx.core.buffer.Buffer
import io.vertx.scala.ext.web.client.HttpResponse

/**
  * Created by leonardo on 7/17/17.
  */
case class ResponseException(statusCode: Int, text: String) extends RuntimeException {

  def status() = statusCode

  def message() = text

  override def toString: String = s"ResponseException(${status}, ${text})"
}

object ResponseException {

  def apply(ex: Throwable): ResponseException = new ResponseException(500, ex.getMessage)

  def apply(response: HttpResponse[Buffer]): ResponseException =
    new ResponseException(response.statusCode(), response.bodyAsString("UTF-8").getOrElse("Empty response"))

}
