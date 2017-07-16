package com.github.leosilvadev.sdashboard.component.domains

import com.github.leosilvadev.sdashboard.task.domains.{HttpTask, Task}
import io.vertx.lang.scala.json.JsonObject

/**
  * Created by leonardo on 7/9/17.
  */
case class Component(name: String, tasks: List[Task] = List.empty, status: Status = Status.Active()) {

  def addTask(json: JsonObject): Component = Component(name, HttpTask(this, json) :: tasks, status)

}

object Component {

  def apply(json: JsonObject): Component = {
    var component = Component(json.getString("name"))
    val tasksJson = json.getJsonArray("tasks")
    tasksJson.forEach(json => {
      component = component.addTask(json.asInstanceOf[JsonObject])
    })
    component
  }

}
