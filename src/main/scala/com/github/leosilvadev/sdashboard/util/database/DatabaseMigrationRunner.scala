package com.github.leosilvadev.sdashboard.util.database

import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.mongo.{IndexOptions, MongoClient}

/**
  * Created by leonardo on 8/5/17.
  */
case class DatabaseMigrationRunner(client: MongoClient) {

  def migrate(): Unit = {
    val options = IndexOptions.fromJson(Json.obj(("unique", true)))
    client.createIndexWithOptions("Admins", Json.obj(("username", 1)), options, result => {})
    client.createIndexWithOptions("Components", Json.obj(("name", 1)), options, result => {})
    client.createIndexWithOptions("Dashboards", Json.obj(("name", 1)), options, result => {})
  }

}
