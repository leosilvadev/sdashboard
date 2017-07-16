package com.github.leosilvadev.sdashboard.configuration.domains

import com.github.leosilvadev.sdashboard.component.domains.Component
import io.vertx.lang.scala.json.JsonObject

/**
  * Created by leonardo on 7/11/17.
  */
case class Configuration(components: List[Component]) {

}

object Configuration {

  def apply(json: JsonObject): Configuration = {
    val componentsJson = json.getJsonArray("components")
    var components = List[Component]()
    componentsJson.forEach(json => {
      components = Component(json.asInstanceOf[JsonObject]) :: components
    })
    Configuration(components)
  }
  
}
