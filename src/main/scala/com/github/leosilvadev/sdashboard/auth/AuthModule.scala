package com.github.leosilvadev.sdashboard.auth

import com.github.leosilvadev.sdashboard.auth.handlers.AdminAuthenticationHandler
import com.github.leosilvadev.sdashboard.auth.middlewares.AuthorizationMiddleware
import com.github.leosilvadev.sdashboard.auth.util.AdminMigrationRunner
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.auth.jwt.JWTAuth
import io.vertx.scala.ext.auth.mongo.MongoAuth
import io.vertx.scala.ext.mongo.MongoClient

/**
  * Created by leonardo on 7/30/17.
  */
case class AuthModule(mongoClient: MongoClient)(implicit vertx: Vertx) {

  val authProvider: MongoAuth = MongoAuth.create(mongoClient, Json.obj(("collectionName", "Admins")))

  val jWTAuth: JWTAuth = JWTAuth.create(vertx,
    Json.obj(
      ("keyStore", Json.obj(
        ("path", "keystore.jceks"),
        ("type", "jceks"),
        ("password", System.getenv().getOrDefault("JWT_SECRET", "secret")))
      )
    )
  )

  val adminAuthenticationHandler = AdminAuthenticationHandler(authProvider, jWTAuth)
  val authorizationMiddleware = AuthorizationMiddleware(jWTAuth)

  val router = AuthRouter(adminAuthenticationHandler)

  AdminMigrationRunner(authProvider).migrate()
  
}
