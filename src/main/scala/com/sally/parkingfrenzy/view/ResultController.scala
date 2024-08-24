package com.sally.parkingfrenzy.view

import com.sally.parkingfrenzy.ParkingFrenzy
import com.sally.parkingfrenzy.ParkingFrenzy.gameRecord
import com.sally.parkingfrenzy.model.GameRecord.getPlayerRank
import com.sally.parkingfrenzy.util.DateTimeUtils.formatElapsedTime
import scalafx.scene.control.Label
import scalafxml.core.macros.sfxml

@sfxml
class ResultController(private val rankLabel: Label,
                       private val timeTakenLabel: Label) {

  rankLabel.text = getPlayerRank(gameRecord).toString
  timeTakenLabel.text = formatElapsedTime(gameRecord._timeTaken)

  // Bring user back to home page when the back to home button is clicked
  def handleBackToHome() : Unit = {
    ParkingFrenzy.playButtonClickSound()
    ParkingFrenzy.showHome()
  }

  // Display game page when the play again button is clicked
  def handlePlayAgain() : Unit = {
    ParkingFrenzy.playButtonClickSound()
    ParkingFrenzy.showGame()
  }

  // Display leaderboard when see leaderboard button is clicked
  def handleSeeLeaderBoard() : Unit = {
    ParkingFrenzy.playButtonClickSound()
    ParkingFrenzy.showLeaderboard()
  }

}
