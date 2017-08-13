package com.github.leosilvadev.sdashboard.component.handlers

import com.github.leosilvadev.sdashboard.component.domains.Component
import com.github.leosilvadev.sdashboard.component.service.ComponentRepository
import com.github.leosilvadev.sdashboard.util.Response
import com.mongodb.MongoWriteException
import io.vertx.core.Handler
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.web.RoutingContext

/**
  * Created by leonardo on 7/19/17.
  */
case class ComponentRegisterHandler(repository: ComponentRepository) extends Handler[RoutingContext] {

  override def handle(context: RoutingContext): Unit = {
    val response = Response(context)
    val component = Component(context.getBodyAsJson().getOrElse(Json.emptyObj()))
    val invalidFields = component.invalid
    if (invalidFields.nonEmpty) {
      response.badRequest(invalidFields)
      return
    }
    repository.register(component)
      .map[Component](component.withId(_))
      .doOnSuccess(persisted => {
        context.vertx().eventBus().send("components.register", persisted.toJson)
      })
      .subscribe(c => response.created(c.id), ex => {
        ex match {
          case _: MongoWriteException => {
            response.badRequest(List("name"))
          }
          case error: Exception => {
            response.internalError(error)
          }
        }
      })
  }

}
