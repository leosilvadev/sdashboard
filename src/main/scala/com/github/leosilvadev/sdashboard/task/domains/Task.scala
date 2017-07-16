package com.github.leosilvadev.sdashboard.task.domains

import com.github.leosilvadev.sdashboard.component.domains.Status
import io.reactivex.Observable
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.Vertx

/**
  * Created by leonardo on 7/9/17.
  */
trait Task {

  def start(vertx: Vertx): Observable[Status]

  def toJson: JsonObject

}
