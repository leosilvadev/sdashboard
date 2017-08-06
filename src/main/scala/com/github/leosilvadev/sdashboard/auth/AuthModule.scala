package com.github.leosilvadev.sdashboard.auth

import com.github.leosilvadev.sdashboard.auth.handlers.AdminAuthenticationHandler
import com.github.leosilvadev.sdashboard.auth.middlewares.AuthorizationMiddleware
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.auth.jwt.JWTAuth
import io.vertx.scala.ext.auth.mongo.MongoAuth
import io.vertx.scala.ext.mongo.MongoClient

/**
  * Created by leonardo on 7/30/17.
  */
case class AuthModule(vertx: Vertx, mongoClient: MongoClient) {

  lazy val authProvider = MongoAuth.create(mongoClient, Json.obj(("collectionName", "Admins")))

  lazy val jWTAuth = JWTAuth.create(vertx,
    Json.obj(
      ("keyStore", Json.obj(
        ("path", "keystore.jceks"),
        ("type", "jceks"),
        ("password", System.getenv().getOrDefault("JWT_SECRET", "secret")))
      )
    )
  )

  lazy val adminAuthenticationHandler = AdminAuthenticationHandler(authProvider, jWTAuth)
  lazy val authorizationMiddleware = AuthorizationMiddleware(jWTAuth)

  lazy val router = AuthRouter(vertx, adminAuthenticationHandler)

}
