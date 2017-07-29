package com.github.leosilvadev.sdashboard.component.service

import com.github.leosilvadev.sdashboard.component.domains.{Component, Status}
import com.github.leosilvadev.sdashboard.task.services.TaskExecutor
import io.reactivex.Observable
import io.reactivex.Observable._

import scala.collection.JavaConverters._

/**
  * Created by leonardo on 7/11/17.
  */
case class ComponentChecker(taskExecutor: TaskExecutor) {

  def start(component: Component): Observable[Status] = merge(component.tasks.map(taskExecutor.execute).asJava)

}
