package com.github.leosilvadev.sdashboard.component.service


import com.github.leosilvadev.sdashboard.component.domains.Component
import io.reactivex.Single
import io.vertx.lang.scala.VertxExecutionContext
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.mongo.MongoClient

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Created by leonardo on 7/18/17.
  */
case class ComponentRepository(vertx: Vertx, client: MongoClient) {

  implicit val ec: ExecutionContext = VertxExecutionContext(vertx.getOrCreateContext())
  val collection = "Components"

  def register(component: Component): Single[String] = {
    Single.create(emitter => {
      client.saveFuture(collection, component.toJson).onComplete {
        case Success(_) => emitter.onSuccess(component.id)
        case Failure(ex) => emitter.onError(ex)
      }
    })
  }

  def unregister(id: String): Single[String] = {
    Single.create(emitter => {
      client.removeFuture(collection, Json.obj(("_id", id))).onComplete {
        case Success(_) => emitter.onSuccess(id)
        case Failure(ex) => emitter.onError(ex)
      }
    })
  }

  def list(query: JsonObject = Json.emptyObj()): Single[List[Component]] = {
    Single.create(emitter => {
      client.findFuture(collection, query).onComplete {
        case Success(result) => emitter.onSuccess(result.map(Component.apply).toList)
        case Failure(ex) => emitter.onError(ex)
      }
    })
  }

}
