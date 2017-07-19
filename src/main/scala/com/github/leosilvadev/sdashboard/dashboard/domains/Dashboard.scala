package com.github.leosilvadev.sdashboard.dashboard.domains

import java.util.concurrent.ConcurrentHashMap

import com.github.leosilvadev.sdashboard.component.domains.Status.Offline
import com.github.leosilvadev.sdashboard.component.domains.{Component, Status}
import com.github.leosilvadev.sdashboard.component.service.ComponentChecker
import com.github.leosilvadev.sdashboard.task.exceptions.ResponseException
import io.reactivex.disposables.Disposable
import io.vertx.scala.core.Vertx

/**
  * Created by leonardo on 7/16/17.
  */
case class Dashboard(vertx: Vertx) {

  val components = new ConcurrentHashMap[String, Disposable]()

  def register(component: Component): Unit = {
    val obs = ComponentChecker(vertx, component).start
    val disposable = obs.subscribe(publish(_), publishOffline(component, _))
    components.put(component.id, disposable)
  }

  def unregister(id: String): Unit = {
    if (!components.containsKey(id)) {
      return
    }

    components.get(id).dispose()
    components.remove(id)
  }

  def publishOffline(component: Component, ex: Throwable): Unit = publish(Offline(component, ResponseException(ex)))

  def publish(status: Status): Unit = vertx.eventBus().publish("components.status.update", status.toJson)

}
