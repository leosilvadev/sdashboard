package com.github.leosilvadev.sdashboard.auth

import com.github.leosilvadev.sdashboard.auth.handlers.AuthorizationHandler
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.auth.mongo.MongoAuth
import io.vertx.scala.ext.mongo.MongoClient

/**
  * Created by leonardo on 7/30/17.
  */
case class AuthModule(vertx: Vertx, mongoClient: MongoClient) {

  lazy val provider = MongoAuth.create(mongoClient, Json.emptyObj())

  lazy val authorizationHandler = AuthorizationHandler(vertx, provider)

}
