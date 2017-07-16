package com.github.leosilvadev.sdashboard.component.domains

import com.github.leosilvadev.sdashboard.task.domains.{HttpTask, Task}
import io.vertx.lang.scala.json.JsonObject

/**
  * Created by leonardo on 7/9/17.
  */
case class Component(name: String, tasks: List[Task] = List.empty, status: Status = Status.Active()) {

  def addTask(task: Task): Component = Component(name, task :: tasks, status)

}

object Component {

  def apply(json: JsonObject): Component = {
    val tasksJson = json.getJsonArray("tasks")
    var tasks = List[Task]()
    tasksJson.forEach(json => {
      tasks = HttpTask(json.asInstanceOf[JsonObject]) :: tasks
    })
    Component(json.getString("name"), tasks)
  }

}
