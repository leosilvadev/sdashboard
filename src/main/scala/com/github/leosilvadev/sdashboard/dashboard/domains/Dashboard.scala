package com.github.leosilvadev.sdashboard.dashboard.domains

import java.util.UUID

import com.github.leosilvadev.sdashboard.component.domains.Component
import com.typesafe.scalalogging.Logger
import io.vertx.core.json.JsonArray
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.core.Vertx

import scala.collection.mutable

/**
  * Created by leonardo on 7/16/17.
  */
case class Dashboard(id: String, name: String, components: List[Component]) {

  val logger = Logger(classOf[Dashboard])

  def withId(id: String): Dashboard = {
    Dashboard(id, name, components.map(_.withDashboardId(id)))
  }

  def addComponent(component: Component): Dashboard = {
    Dashboard(id, name, component :: components)
  }

  def removeComponent(id: String): Dashboard = {
    Dashboard(id, name, components.filterNot(component => id.eq(component.id)))
  }

  def toJson: JsonObject = {
    Json.obj(
      ("_id", id),
      ("name", name),
      ("components", components.map(_.toJson))
    )
  }

}

case object Dashboard {

  def apply(json: JsonObject)(implicit vertx: Vertx): Dashboard = {
    val id = json.getString("_id", UUID.randomUUID().toString)
    val name = json.getString("name", "")
    val componentsJson = json.getJsonArray("components", new JsonArray())

    if (name.isEmpty || componentsJson.isEmpty) {
      throw new RuntimeException("Invalid dashboard data")
    }

    val buffer = mutable.Buffer[Component]()
    componentsJson.forEach(item => {
      buffer += Component(item.asInstanceOf[JsonObject])
    })
    Dashboard(id, name, buffer.toList)
  }

}
