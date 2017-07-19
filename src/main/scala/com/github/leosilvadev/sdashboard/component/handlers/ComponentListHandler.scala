package com.github.leosilvadev.sdashboard.component.handlers

import com.github.leosilvadev.sdashboard.component.service.ComponentRepository
import com.github.leosilvadev.sdashboard.util.Response
import io.vertx.core.Handler
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.web.RoutingContext

/**
  * Created by leonardo on 7/18/17.
  */
case class ComponentListHandler(repository: ComponentRepository) extends Handler[RoutingContext] {

  override def handle(context: RoutingContext): Unit = {
    val response = Response(context)
    repository.list(Json.emptyObj()).subscribe(components => {
      val json = components.foldLeft(Json.emptyArr())(_ add _.toJson)
      response.ok(json)

    }, response.internalError(_))
  }

}
