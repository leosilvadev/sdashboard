package com.github.leosilvadev.sdashboard.task

import com.github.leosilvadev.sdashboard.task.services.TaskExecutor
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.client.WebClient

/**
  * Created by leonardo on 7/29/17.
  */
case class TaskModule(vertx: Vertx, client: WebClient) {

  lazy val executor = TaskExecutor(vertx, client)

}
