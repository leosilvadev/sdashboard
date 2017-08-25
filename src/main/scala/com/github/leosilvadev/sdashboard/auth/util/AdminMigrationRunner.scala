package com.github.leosilvadev.sdashboard.auth.util

import com.github.leosilvadev.sdashboard.auth.domains.Admin
import com.typesafe.scalalogging.Logger
import io.vertx.lang.scala.VertxExecutionContext
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.auth.mongo.MongoAuth

import scala.collection.mutable
import scala.util.{Failure, Success}

/**
  * Created by leonardo on 8/25/17.
  */
case class AdminMigrationRunner(authProvider: MongoAuth)(implicit vertx: Vertx) {

  implicit val executionContext = VertxExecutionContext(vertx.getOrCreateContext())

  val logger = Logger(classOf[AdminMigrationRunner])

  def migrate(): Unit = {
    val email = System.getenv().getOrDefault("ADMIN_EMAIL", "")
    val password = System.getenv().getOrDefault("ADMIN_PASS", "")

    if (email.isEmpty || password.isEmpty) {
      logger.info("No default admin was given to be registered.")
      return
    }

    authProvider.authenticateFuture(Admin(email, password).toJson).onComplete {
      case Success(_) => logger.info("Default admin already registered.")
      case Failure(_) =>
        authProvider.insertUserFuture(email, password, mutable.Buffer.empty, mutable.Buffer.empty).onComplete {
          case Success(_) => logger.info("Default admin {} registered successfully", email)
          case Failure(ex) => logger.error(ex.getMessage, ex)
        }
    }
  }

}
