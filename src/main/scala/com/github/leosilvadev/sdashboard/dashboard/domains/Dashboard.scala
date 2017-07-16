package com.github.leosilvadev.sdashboard.dashboard.domains

import com.github.leosilvadev.sdashboard.configuration.domains.Configuration
import io.vertx.scala.core.Vertx

/**
  * Created by leonardo on 7/16/17.
  */
case class Dashboard(vertx: Vertx, configuration: Configuration) {

  def init(): Unit = {

  }

}
