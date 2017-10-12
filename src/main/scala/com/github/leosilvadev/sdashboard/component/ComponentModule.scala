package com.github.leosilvadev.sdashboard.component

import com.github.leosilvadev.sdashboard.component.service.{ComponentChecker, ComponentStatusRepository, ComponentStatusUpdater}
import com.github.leosilvadev.sdashboard.task.TaskModule
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.mongo.MongoClient

/**
  * Created by leonardo on 7/29/17.
  */
case class ComponentModule(client: MongoClient, taskModule: TaskModule)(implicit vertx: Vertx) {

  val repository = ComponentStatusRepository(client)
  val checker = ComponentChecker(taskModule.executor)
  checker.start()

  val componentStatusUpdater = ComponentStatusUpdater(repository)
  componentStatusUpdater.start()

}
