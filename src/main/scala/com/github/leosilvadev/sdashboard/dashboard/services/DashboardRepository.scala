package com.github.leosilvadev.sdashboard.dashboard.services

import java.util.function.{Function => JFunction, Predicate => JPredicate}

import com.github.leosilvadev.sdashboard.dashboard.domains.Dashboard
import com.typesafe.scalalogging.Logger
import io.reactivex.{Observable, Single}
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
      val json = dashboard.toJson
      client.updateFuture(collection, Json.obj(("_id", dashboard.id)), Json.obj(("$set", json))).onComplete {
        case Success(_) => emitter.onSuccess(dashboard)
        case Failure(ex) => emitter.onError(ex)
      }
    })
  }

  def registerIfNotExist(newDashboard: Dashboard): Single[Dashboard] = {
    findOneByName(newDashboard.name)
      .flatMap[Dashboard](dashboard => update(newDashboard.withId(dashboard.id)))
      .doOnError(error => {
        logger.error(error.getMessage, error)
      })
      .onErrorResumeNext(register(newDashboard))
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
        case Success(json) if null != json =>
          emitter.onSuccess(Dashboard(json))

        case Success(_) =>
          emitter.onError(new RuntimeException("Invalid dashboard"))

        case Failure(ex) =>
          emitter.onError(ex)
      }
    })
  }

  def findAll(): Observable[Dashboard] = {
    Observable.create(emitter => {
      client.findFuture(collection, Json.emptyObj()).onComplete {
        case Success(json) =>
          json.map(Dashboard(_)).foreach(emitter.onNext)
          emitter.onComplete()

        case Failure(ex) =>
          emitter.onError(ex)
      }
    })
  }

}
