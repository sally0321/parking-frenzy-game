package com.sally.parkingfrenzy.view

import com.sally.parkingfrenzy.ParkingFrenzy
import com.sally.parkingfrenzy.ParkingFrenzy.{createMediaPlayer, playerName, selectedCar}
import com.sally.parkingfrenzy.model.PlayerCar
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, TextField, ToggleButton, ToggleGroup}
import scalafx.scene.media.{Media, MediaPlayer}
import scalafxml.core.macros.sfxml

@sfxml
class SelectCarController(private var sportCarToggleButton: ToggleButton,
                          private var policeCarToggleButton: ToggleButton,
                          private var taxiToggleButton: ToggleButton,
                          private val selectCarToggleGroup: ToggleGroup,
                          private val nameTextField: TextField) {

  // Create media players for sound effects
  private val taxiClickSoundPlayer: MediaPlayer = createMediaPlayer("audio/taxi-sound.mp3", 1)
  private val policeCarClickSoundPlayer: MediaPlayer = createMediaPlayer("audio/police-car-sound.mp3", 1)
  private val sportCarClickSoundPlayer: MediaPlayer = createMediaPlayer("audio/sport-car-sound.mp3", 1)

  // Check if player has selected a car and which car is selected when the start button is clicked for input validation
  private def getSelectedCar: Boolean = {
    selectCarToggleGroup.getSelectedToggle match{
      case taxi if taxiToggleButton.isSelected=> {
        selectedCar = PlayerCar.taxi
        true
      }
      case policeCar if policeCarToggleButton.isSelected => {
        selectedCar = PlayerCar.policeCar
        true
      }
      case sportCar if sportCarToggleButton.isSelected => {
        selectedCar = PlayerCar.sportCar
        true
      }
      // Display warning dialog if no car is selected
      case _ => {
        new Alert(AlertType.Warning) {
          title = "Whoops! No car is selected."
          headerText = "You can't drive without a car!"
          contentText = "Looks like you forgot to select your car... Select your car before hitting the road!"
        }.showAndWait()
        false
      }
    }
  }

  // Check if player has entered their name when the start button is selected for input validation
  private def getPlayerName: Boolean = {
    val name = nameTextField.getText.trim // Get and trim the text from the nameField
    // Display warning dialog if no name is entered
    if (name.isEmpty) {
      new Alert(AlertType.Warning) {
        title = "Whoops! Player is unknown."
        headerText = "Don't be a mystery driver!"
        contentText = "Looks like you forgot to tell us who you are... Enter your name before hitting the road!"
      }.showAndWait()
      false
    } else {
      playerName = name
      true
    }
  }

  // Display game page if the player has selected a car and entered name when the start button is clicked
  def handleStart(): Unit = {
    ParkingFrenzy.playButtonClickSound()
    if (getSelectedCar && getPlayerName) ParkingFrenzy.showGame()
  }

  // Bring player back to home page when the back button is clicked
  def handleBack(): Unit = {
    ParkingFrenzy.playButtonClickSound()
    ParkingFrenzy.showHome()
  }

  // Play sound effect when taxi toggle button is clicked
  def playTaxiClickSound(): Unit = {
    taxiClickSoundPlayer.stop()
    taxiClickSoundPlayer.play()
  }

  // Play sound effect when police car toggle button is clicked
  def playPoliceCarClickSound(): Unit = {
    policeCarClickSoundPlayer.stop()
    policeCarClickSoundPlayer.play()
  }

  // Play sound effect when sport car toggle button is clicked
  def playSportCarClickSound(): Unit = {
    sportCarClickSoundPlayer.stop()
    sportCarClickSoundPlayer.play()
  }
}
