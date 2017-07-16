package com.github.leosilvadev.sdashboard.component.service

import com.github.leosilvadev.sdashboard.component.domains.{Component, Status}
import io.reactivex.Observable
import io.reactivex.Observable._
import io.vertx.scala.core.Vertx

import scala.collection.JavaConverters._

/**
  * Created by leonardo on 7/11/17.
  */
case class ComponentChecker(vertx: Vertx, component: Component) {

  def start: Observable[Status] = merge(component.tasks.map(_.start(vertx)).asJava)

}
