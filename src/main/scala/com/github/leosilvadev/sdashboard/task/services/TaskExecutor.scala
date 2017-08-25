package com.github.leosilvadev.sdashboard.task.services

import com.github.leosilvadev.sdashboard.component.domains.Status
import com.github.leosilvadev.sdashboard.task.domains.Task
import io.reactivex.Observable
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.client.WebClient

/**
  * Created by leonardo on 7/29/17.
  */
case class TaskExecutor(client: WebClient)(implicit vertx: Vertx) {

  def execute(task: Task): Observable[Status] = {
    Observable.create[Status](emitter => {
      vertx.setPeriodic(task.getFrequency, n => {
        task.run(client, emitter)
      })
    })
  }

}
