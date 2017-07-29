package com.github.leosilvadev.sdashboard.dashboard.services

import com.github.leosilvadev.sdashboard.component.service.ComponentRepository
import com.github.leosilvadev.sdashboard.dashboard.domains.Dashboard
import com.github.leosilvadev.sdashboard.task.services.TaskExecutor
import io.vertx.core.logging.LoggerFactory
import io.vertx.scala.core.Vertx

/**
  * Created by leonardo on 7/29/17.
  */
case class DashboardBuilder(vertx: Vertx, componentRepository: ComponentRepository, taskExecutor: TaskExecutor) {

  val logger = LoggerFactory.getLogger(classOf[DashboardBuilder])

  def build(): Dashboard ={
    val dashboard = Dashboard(vertx, taskExecutor)
    componentRepository.list().subscribe(dashboard.reloadComponents(_), ex => logger.error(ex.getMessage, ex))
    dashboard
  }

}
