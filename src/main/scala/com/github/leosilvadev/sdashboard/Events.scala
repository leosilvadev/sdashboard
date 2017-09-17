package com.github.leosilvadev.sdashboard

/**
  * Created by leonardo on 9/17/17.
  */

object Events {

  object component {

    def newOne: String = "components.new"
    def registrationSucceeded: String = "components.registration.succeeded"
    def registrationFailed: String = "components.registration.failed"

    def checkSuceeded: String = "components.check.succeeded"
    def checkFailed: String = "components.check.failed"

  }

}
