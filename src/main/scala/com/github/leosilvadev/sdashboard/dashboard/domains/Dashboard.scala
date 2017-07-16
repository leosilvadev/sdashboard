package com.github.leosilvadev.sdashboard.dashboard.domains

import com.github.leosilvadev.sdashboard.component.domains.Status
import com.github.leosilvadev.sdashboard.component.service.ComponentChecker
import io.reactivex.Observable
import io.reactivex.Observable._
import io.vertx.scala.core.Vertx

import scala.collection.JavaConverters._

/**
  * Created by leonardo on 7/16/17.
  */
case class Dashboard(vertx: Vertx, configuration: Configuration) {

  def start(): Observable[Status] = {
    merge(configuration.components.map(ComponentChecker(vertx, _).start).asJava)
  }

}
