package com.github.leosilvadev.sdashboard.component.service


import com.github.leosilvadev.sdashboard.component.domains.Component
import io.reactivex.Single
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.mongo.MongoClient

/**
  * Created by leonardo on 7/18/17.
  */
case class ComponentRepository(vertx: Vertx, client: MongoClient) {

  val collection = "Components"

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

}
