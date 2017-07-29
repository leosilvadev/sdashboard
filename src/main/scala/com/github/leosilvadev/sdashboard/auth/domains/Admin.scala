package com.github.leosilvadev.sdashboard.auth.domains

import io.vertx.lang.scala.json.{Json, JsonObject}

import scala.collection.mutable

/**
  * Created by leonardo on 7/29/17.
  */
case class Admin(email: String, password: String,
                 roles: mutable.Buffer[String] = mutable.Buffer.empty,
                 permissions: mutable.Buffer[String] = mutable.Buffer.empty) {


  def toJson: JsonObject = {
    Json.obj(("username", email), ("password", password))
  }

  def toTuple: (String, String, mutable.Buffer[String], mutable.Buffer[String]) = {
    (email, password, roles, permissions)
  }

}