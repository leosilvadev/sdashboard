package com.github.leosilvadev.sdashboard.component.domains

import java.time.Instant
import java.util.UUID

import com.github.leosilvadev.sdashboard.component.domains.Status.Active
import io.vertx.lang.scala.json.{Json, JsonObject}

/**
  * Created by leonardo on 8/13/17.
  */
case class ComponentSnapshot(id: String, componentId: String, data: JsonObject, error: String, lastUpdate: Instant) {

  def toJson: JsonObject = {
    Json.obj(("_id", id), ("component_id", componentId), ("data", data), ("error", error), ("last_update", lastUpdate.toString))
  }

  def isSameResponseOf(snapshot: ComponentSnapshot): Boolean = {
    (error.nonEmpty && snapshot.error.nonEmpty && error.equals(snapshot.error)) ||
      (!data.isEmpty && !snapshot.data.isEmpty && data.equals(snapshot.data))
  }

  def setLastUpdate(): ComponentSnapshot = {
    new ComponentSnapshot(id, componentId, data, error, Instant.now())
  }
}

object ComponentSnapshot {

  def apply(componentId: String, data: JsonObject, error: String, lastUpdate: Instant): ComponentSnapshot =
    new ComponentSnapshot(UUID.randomUUID().toString, componentId, data, error, lastUpdate)

  def apply(json: JsonObject): ComponentSnapshot = {
    new ComponentSnapshot(
      json.getString("_id", UUID.randomUUID().toString),
      json.getString("component_id", ""),
      json.getJsonObject("data", Json.emptyObj()),
      json.getString("error", ""),
      json.getInstant("last_update", Instant.now())
    )
  }

  def apply(status: Status): Option[ComponentSnapshot] = {
    status match {
      case Active() => None
      case _ =>
        val json = status.toJson
        val componentId = json.getJsonObject("component", Json.emptyObj()).getString("_id", "")
        val data = json.getJsonObject("data", Json.emptyObj())
        val error = json.getString("error", "")
        val lastUpdate = json.getInstant("datetime", Instant.now())
        Some(ComponentSnapshot(componentId, data, error, lastUpdate))
    }
  }

}
