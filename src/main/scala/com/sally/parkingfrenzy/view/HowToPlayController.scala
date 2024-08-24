package com.sally.parkingfrenzy.view

import com.sally.parkingfrenzy.ParkingFrenzy
import scalafxml.core.macros.sfxml

@sfxml
class HowToPlayController {
  // Bring player back to home page when the back button is clicked
  def handleBack(): Unit = {
    ParkingFrenzy.playButtonClickSound()
    ParkingFrenzy.showHome()
  }
}
