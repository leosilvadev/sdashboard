package com.github.leosilvadev.sdashboard.component.service


import com.github.leosilvadev.sdashboard.component.domains.ComponentSnapshot
import com.typesafe.scalalogging.Logger
import io.vertx.lang.scala.VertxExecutionContext
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.mongo.{FindOptions, MongoClient}

import scala.util.{Failure, Success}

/**
  * Created by leonardo on 7/18/17.
  */
case class ComponentStatusRepository(client: MongoClient)(implicit vertx: Vertx) {

  protected implicit val executionContext = VertxExecutionContext(vertx.getOrCreateContext())

  val logger = Logger(classOf[ComponentStatusRepository])
  val collectionStatuses = "ComponentStatuses"

  def createSnapshot(currentSnapshot: ComponentSnapshot): Unit = {
    val query = Json.obj(("component_id", currentSnapshot.componentId))
    val opt = FindOptions().setLimit(1).setSort(Json.obj(("datetime", -1)))
    client.findWithOptionsFuture(collectionStatuses, query, opt).onComplete {
      case Success(snapshots) if snapshots.size == 1 =>
        val lastSnapshot = ComponentSnapshot(snapshots.head)
        if (currentSnapshot.isSameResponseOf(lastSnapshot)) {
          client.replace(collectionStatuses, Json.obj(("_id", lastSnapshot.id)), lastSnapshot.setLastUpdate().toJson, _ => {})

        } else {
          client.insert(collectionStatuses, currentSnapshot.toJson, _ => {})

        }

      case Success(_) =>
        client.insert(collectionStatuses, currentSnapshot.toJson, _ => {})

      case Failure(ex) => logger.error(ex.getMessage, ex)
    }
  }

}
