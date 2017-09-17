package com.github.leosilvadev.sdashboard.dashboard.services

import java.util.function.{Function => JFunction, Predicate => JPredicate}

import com.github.leosilvadev.sdashboard.dashboard.domains.Dashboard
import com.typesafe.scalalogging.Logger
import io.reactivex.Single
import io.vertx.lang.scala.VertxExecutionContext
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.mongo.MongoClient

import scala.util.{Failure, Success}

/**
  * Created by leonardo on 9/6/17.
  */
case class DashboardRepository(client: MongoClient)(implicit vertx: Vertx) {

  protected implicit val executionContext = VertxExecutionContext(vertx.getOrCreateContext())

  val logger = Logger(classOf[DashboardRepository])
  val collection = "Dashboards"

  def register(dashboard: Dashboard): Single[Dashboard] = {
    Single.create(emitter => {
      client.saveFuture(collection, dashboard.toJson).onComplete {
        case Success(_) => emitter.onSuccess(dashboard)
        case Failure(ex) => emitter.onError(ex)
      }
    })
  }

  def update(dashboard: Dashboard): Single[Dashboard] = {
    Single.create(emitter => {
      client.updateFuture(collection, Json.obj(("id", dashboard.id)), dashboard.toJson).onComplete {
        case Success(_) => emitter.onSuccess(dashboard)
        case Failure(ex) => emitter.onError(ex)
      }
    })
  }

  def registerIfNotExist(dashboard: Dashboard): Single[Dashboard] = {
    findOneByName(dashboard.name)
        .flatMap(_ => update(dashboard))
        .doOnError(error => {
          logger.error(error.getMessage, error)
        })
        .onErrorResumeNext(register(dashboard))
  }

  def findOneByName(name: String): Single[Dashboard] = {
    findOneBy("name", name)
  }

  def findOneById(id: String): Single[Dashboard] = {
    findOneBy("_id", id)
  }

  def findOneBy(field: String, value: String): Single[Dashboard] = {
    Single.create(emitter => {
      client.findOneFuture(collection, Json.obj((field, value)), Json.emptyObj()).onComplete {
        case Success(json) if null != json => emitter.onSuccess(Dashboard(json))
        case Success(_) => emitter.onError(new RuntimeException("Invalid dashboard"))
        case Failure(ex) => emitter.onError(ex)
      }
    })
  }

}
