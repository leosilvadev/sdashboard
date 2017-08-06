package com.github.leosilvadev.sdashboard.auth.util

import com.github.leosilvadev.sdashboard.auth.domains.Admin
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.web.RoutingContext

/**
  * Created by leonardo on 8/5/17.
  */
object Auth {

  def tokenFromHeader(context: RoutingContext): Option[String] = {
    val authorization = context.request().headers().get("Authorization").getOrElse("")
    if (authorization.contains("Bearer")) {
      return Some(authorization.replace("Bearer ", ""))
    }

    None
  }

  def adminFromBody(context: RoutingContext): Option[Admin] = {
    val body = context.getBodyAsJson().getOrElse(Json.emptyObj())
    val username = body.getString("username", "")
    val password = body.getString("password", "")
    if (username.nonEmpty && password.nonEmpty) {
      return Some(Admin(username, password))
    }

    None
  }

}
