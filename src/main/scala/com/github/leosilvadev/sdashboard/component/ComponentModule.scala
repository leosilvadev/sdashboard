package com.github.leosilvadev.sdashboard.component

import com.github.leosilvadev.sdashboard.component.handlers.{ComponentListHandler, ComponentRegisterHandler, ComponentUnregisterHandler}
import com.github.leosilvadev.sdashboard.component.service.{ComponentChecker, ComponentRepository}
import com.github.leosilvadev.sdashboard.task.TaskModule
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.mongo.MongoClient

/**
  * Created by leonardo on 7/29/17.
  */
case class ComponentModule(vertx: Vertx, client: MongoClient, taskModule: TaskModule) {

  lazy val repository = ComponentRepository(client)
  lazy val checker = ComponentChecker(taskModule.executor)

  lazy val listHandler = ComponentListHandler(repository)
  lazy val registerHandler = ComponentRegisterHandler(repository)
  lazy val unregisterHandler = ComponentUnregisterHandler(repository)

  lazy val router = ComponentRouter(vertx, listHandler, registerHandler, unregisterHandler)

}