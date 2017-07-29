package com.github.leosilvadev.sdashboard

import com.github.leosilvadev.sdashboard.auth.AuthModule
import com.github.leosilvadev.sdashboard.component.ComponentModule
import com.github.leosilvadev.sdashboard.dashboard.DashboardModule
import com.github.leosilvadev.sdashboard.task.TaskModule
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.mongo.MongoClient

/**
  * Created by leonardo on 7/29/17.
  */
case class Modules(vertx: Vertx, mongoClient: MongoClient) {

  lazy val task = TaskModule(vertx)
  lazy val component = ComponentModule(vertx, mongoClient, task)
  lazy val dashboard = DashboardModule(vertx, component)
  lazy val auth = AuthModule(vertx, mongoClient)

}
