package com.github.leosilvadev.sdashboard.component.domains

import java.util.UUID

import com.github.leosilvadev.sdashboard.task.domains.{HttpTask, Task}
import io.vertx.lang.scala.json.{Json, JsonObject}

import scala.collection.mutable

/**
  * Created by leonardo on 7/9/17.
  */
case class Component(id: String, name: String, dashboardId: String, tasks: List[Task] = List.empty, status: Status = Status.Active()) {

  def addTask(json: JsonObject): Component = Component(id, name, dashboardId, HttpTask(this, json) :: tasks, status)

  def withId(id: String): Component = {
    Component(id, name, dashboardId, tasks, status)
  }

  def toJson: JsonObject = {
    Json.obj(("_id", id), ("name", name), ("dashboardId", dashboardId), ("tasks", tasks.map(_.toJson)))
  }

  def invalid: List[String] = {
    val fields = mutable.MutableList[String]()

    if (name.isEmpty)
      fields += "name"

    if (tasks.isEmpty)
      fields += "tasks"

    fields.toList
  }

}

object Component {

  def apply(json: JsonObject): Component = {
    var component = Component(json.getString("_id", UUID.randomUUID().toString), json.getString("name"), json.getString("dashboardId"))
    val tasksJson = json.getJsonArray("tasks", Json.emptyArr())
    tasksJson.forEach(json => {
      component = component.addTask(json.asInstanceOf[JsonObject])
    })
    component
  }

  def apply(id: String, name: String, dashboardId: String): Component = new Component(id, name, dashboardId)

}
