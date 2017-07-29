package com.github.leosilvadev.sdashboard.dashboard.services

import com.github.leosilvadev.sdashboard.component.service.ComponentRepository
import com.github.leosilvadev.sdashboard.dashboard.domains.Dashboard
import io.vertx.core.logging.LoggerFactory

/**
  * Created by leonardo on 7/29/17.
  */
case class DashboardLoader(componentRepository: ComponentRepository) {

  val logger = LoggerFactory.getLogger(classOf[DashboardLoader])

  def load(dashboard: Dashboard): Unit ={
    componentRepository.list().subscribe(dashboard.reloadComponents(_), ex => logger.error(ex.getMessage, ex))
  }

}
