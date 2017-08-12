package com.github.leosilvadev.sdashboard.component.service


import com.github.leosilvadev.sdashboard.component.domains.{Component, ComponentSnapshot}
import com.typesafe.scalalogging.Logger
import io.reactivex.Single
import io.vertx.lang.scala.VertxExecutionContext
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.mongo.{FindOptions, MongoClient}

import scala.util.{Failure, Success}

/**
  * Created by leonardo on 7/18/17.
  */
case class ComponentRepository(vertx: Vertx, client: MongoClient) {

  protected implicit val executionContext = VertxExecutionContext(vertx.getOrCreateContext())

  val logger = Logger(classOf[ComponentRepository])
  val collection = "Components"
  val collectionStatuses = "ComponentStatuses"

  def register(component: Component): Single[String] = {
    Single.create(emitter => {
      client.save(collection, component.toJson, result => {
        if (result.succeeded()) {
          emitter.onSuccess(component.id)
        } else {
          emitter.onError(result.cause())
        }
      })
    })
  }

  def unregister(id: String): Single[String] = {
    Single.create(emitter => {
      client.remove(collection, Json.obj(("_id", id)), result => {
        if (result.succeeded()) {
          emitter.onSuccess(id)
        } else {
          emitter.onError(result.cause())
        }
      })
    })
  }

  def list(query: JsonObject = Json.emptyObj()): Single[List[Component]] = {
    Single.create(emitter => {
      client.find(collection, query, result => {
        if (result.succeeded()) {
          emitter.onSuccess(result.result().map(Component.apply).toList)
        } else {
          emitter.onError(result.cause())
        }
      })
    })
  }

  def createSnapshot(currentSnapshot: ComponentSnapshot): Unit = {
    val query = Json.obj(("component_id", currentSnapshot.componentId))
    val opt = FindOptions().setLimit(1).setSort(Json.obj(("datetime", -1)))
    client.findWithOptionsFuture(collectionStatuses, query, opt).onComplete {
      case Success(snapshots) if snapshots.size == 1 =>
        val lastSnapshot = ComponentSnapshot(snapshots.head)
        if (currentSnapshot.isSameResponseOf(lastSnapshot)) {
          client.replace(collectionStatuses, Json.obj(("_id", lastSnapshot.id)), lastSnapshot.setLastUpdate().toJson, r => {})

        } else {
          client.insert(collectionStatuses, currentSnapshot.toJson, r => {})

        }

      case Success(s) =>
        client.insert(collectionStatuses, currentSnapshot.toJson, r => {})

      case Failure(ex) => logger.error(ex.getMessage, ex)
    }
  }

}
