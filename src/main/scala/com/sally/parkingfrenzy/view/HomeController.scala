package com.sally.parkingfrenzy.view

import scalafxml.core.macros.sfxml
import com.sally.parkingfrenzy.ParkingFrenzy

@sfxml
class HomeController(){

  // Display select car page when the start button is clicked
  def handleStart(): Unit = {
    ParkingFrenzy.playButtonClickSound()
    ParkingFrenzy.showSelectCar()
  }

  // Display how to play page when the how to play button is clicked
  def handleHowToPlay(): Unit = {
    ParkingFrenzy.playButtonClickSound()
    ParkingFrenzy.showHowToPlay()
  }

  // Display leaderboard page when the leaderboard button is clicked
  def handleLeaderboard(): Unit = {
    ParkingFrenzy.playButtonClickSound()
    ParkingFrenzy.showLeaderboard()
  }
}