package com.github.leosilvadev.sdashboard.task.domains

import com.github.leosilvadev.sdashboard.component.domains.{Component, Status}
import com.github.leosilvadev.sdashboard.task.exceptions.ResponseException
import io.vertx.core.AsyncResult
import io.vertx.core.buffer.Buffer
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.web.client.HttpResponse

/**
  * Created by leonardo on 7/30/17.
  */
trait HttpTaskResponse {

  def status: Status

}

case class HttpTaskSuccessResponse(component: Component, response: HttpResponse[Buffer]) extends HttpTaskResponse {

  def status: Status = {
    try {
      Status.Online(component, response.bodyAsJsonObject().getOrElse(Json.emptyObj()))
    } catch {
      case _: Exception => Status.Offline(component, ResponseException(500, "Status endpoint returned a non-json response"))
    }
  }

}

case class HttpTaskFailureResponse(component: Component, exception: ResponseException) extends HttpTaskResponse {

  def status: Status = {
    Status.Offline(component, exception)
  }

}

object HttpTaskResponse {

  def apply(component: Component, result: AsyncResult[HttpResponse[Buffer]]): HttpTaskResponse = {
    if (result.failed()) {
      return HttpTaskFailureResponse(component, ResponseException(result.cause()))
    }

    def response = result.result()

    if (response.statusCode() > 400) {
      return HttpTaskFailureResponse(component, ResponseException(response))
    }

    HttpTaskSuccessResponse(component, response)
  }

}
