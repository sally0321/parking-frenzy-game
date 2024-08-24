package com.sally.parkingfrenzy.view

import com.sally.parkingfrenzy.model.{CollisionDetector, GameRecord, PlayerCar, Stopwatch}
import scalafx.animation.{AnimationTimer, PauseTransition}
import scalafx.scene.input.KeyCode
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.image.ImageView
import scalafx.scene.image.Image
import scalafx.scene.layout.AnchorPane
import scalafxml.core.macros.sfxml
import com.sally.parkingfrenzy.ParkingFrenzy
import com.sally.parkingfrenzy.ParkingFrenzy.{createMediaPlayer, gameRecord, selectedCar}
import scalafx.scene.control.{Alert, Button, Label, RadioMenuItem, Slider}
import com.sally.parkingfrenzy.util.DateTimeUtils.formatElapsedTime
import scalafx.scene.media.MediaPlayer
import scalafx.util.Duration
import com.sally.parkingfrenzy.model.level._
import scalafx.scene.control.Alert.AlertType

import scala.util.Random

@sfxml
class GameController(private val parkingArea: AnchorPane,
                     private val playerCarImageView: ImageView,
                     private val drivingModeSlider: Slider,
                     private val stopwatchLabel: Label,
                     private val weatherImageView: ImageView,
                     private val pausePlayButton: Button,
                     private val onMusicMenuItem: RadioMenuItem,
                     private val offMusicMenuItem: RadioMenuItem
                     ) {

  // Initialize variables
  private var pause: Boolean = false
  private val stopwatch = new Stopwatch()
  private val raining: Boolean = Random.nextBoolean() // the weather for each gameplay is randomized
  private val playerCar: PlayerCar = selectedCar
  private val parkingLotRect: Rectangle = new Rectangle()
  var currentLevel: Level = Level1

  // Create the main game loop to handle game updates throughout gameplay
  private val game: AnimationTimer = AnimationTimer { _ =>
    handleGameUpdate()
  }

  // Create the loop to update the stopwatch label with the elapsed time
  private val updateStopwatchLabel: AnimationTimer = AnimationTimer { _ =>
    stopwatchLabel.setText(formatElapsedTime(stopwatch.getElapsedTime))
  }

  // Create media players for background music and sound effects
  private val rainingBgmPlayer: MediaPlayer = createMediaPlayer("audio/raining-bgm.mp3", MediaPlayer.Indefinite)
  private val gameBgmPlayer: MediaPlayer = createMediaPlayer("audio/game-bgm.mp3", MediaPlayer.Indefinite)
  private val parkedSuccessfullySoundPlayer: MediaPlayer = createMediaPlayer("audio/parked-successfully-sound.mp3", 1)
  private val carCrashSoundPlayer: MediaPlayer = createMediaPlayer("audio/car-crash-sound.mp3", 1)

  // Play sound effect for successful parking
  private def playParkedSuccessfullySound(): Unit = {
    parkedSuccessfullySoundPlayer.stop()
    parkedSuccessfullySoundPlayer.play()
  }

  // Play sound effect for car collision
  private def playCarCrashSound(): Unit = {
    carCrashSoundPlayer.stop()
    carCrashSoundPlayer.play()
  }

  // Set up the parking lot area based on current level's configuration
  private def setupParkingLot(): Unit = {
    // Parking lot position
    parkingLotRect.setX(currentLevel.parkingLot.xInitial)
    parkingLotRect.setY(currentLevel.parkingLot.yInitial)
    parkingLotRect.setRotate(currentLevel.parkingLot.rotationInitial)

    // Parking lot appearance
    parkingLotRect.setWidth(currentLevel.parkingLot.width)
    parkingLotRect.setHeight(currentLevel.parkingLot.height)
    parkingLotRect.setFill(Color.Transparent)
    parkingLotRect.setStrokeWidth(6)
    parkingLotRect.setStroke(Color.White)

    // Update parking area with the parking lot
    parkingArea.children.remove(parkingLotRect)
    parkingArea.children.add(parkingLotRect)
  }

  // Set up player car based on initial player car configuration
  private def setupPlayerCar(): Unit = {
    // Reset player car position
    playerCar.resetPosition()
    updatePlayerCarImageViewPosition()

    // Update player car appearance
    playerCarImageView.fitWidth = playerCar.width
    playerCarImageView.fitHeight = playerCar.height
    playerCarImageView.image = playerCar.image
    playerCarImageView.toFront()
  }

  // Set up car obstacles on the parking area based on the current level's configuration
  private def setupCarObstacles(): Unit = {
    // Create car obstacles
    val carObstaclesImageViews = currentLevel.carObstacles.map { carObstacle =>
      new ImageView(carObstacle.image) {
        x = carObstacle.xInitial
        y = carObstacle.yInitial
        rotate = carObstacle.rotationInitial
        fitWidth = carObstacle.width
        fitHeight = carObstacle.height
      }
    }
    // Update parking area with the car obstacles
    carObstaclesImageViews.foreach(carObstacle => parkingArea.children.remove(carObstacle))
    carObstaclesImageViews.foreach(carObstacle => parkingArea.children.add(carObstacle))
  }

  // Set up the parking area (game board) with parking lot, player car and car obstacles
  private def setupParkingArea(): Unit = {
    setupParkingLot()
    setupPlayerCar()
    setupCarObstacles()
  }

  // Set up weather effects including visual effect, background music and car bahavior
  private def setupWeather(): Unit = {
    // Reset weather effects
    weatherImageView.image = null
    rainingBgmPlayer.stop()
    playerCar.resetCharacteristics(raining)

    // Apply raining effects if it is raining
    if (raining) {
      weatherImageView.image = new Image(getClass.getResourceAsStream("../images/raining.gif"))
      weatherImageView.toFront()
      rainingBgmPlayer.play()
    }
  }

  // Handle game update throughout the gameplay
  private def handleGameUpdate(): Unit = {
    if (parkedSuccessfully) handleParkedSuccessfully()
    handleInput()
    playerCar.move()
    updatePlayerCarImageViewPosition()
    if (collided) handleCollision()
  }

  // Check if the player has parked the car successfully within the parking lot
  private def parkedSuccessfully: Boolean = {
    CollisionDetector.isWithinParkingLot(playerCar, currentLevel.parkingLot)
  }

  // Handle the event of a successful parking
  private def handleParkedSuccessfully(): Unit = {
    // Sound effect
    playParkedSuccessfullySound()
    // Visual effect: change the parking lot from white to green color
    parkingLotRect.setStroke(Color.Green)
    // Stop running the stopwatch and updating the stopwatch label
    stopwatch.stop()
    updateStopwatchLabel.stop()
    // Stop the main game loop
    game.stop()
    // Create and save the game record with the time taken for a successful parking
    gameRecord = new GameRecord(stopwatch.getElapsedTime)
    gameRecord.save()
    // Display result page after 2s delay
    val resultDisplayDelay = new PauseTransition(Duration(2000)) /* Reference: (Jewelsea, 2023) */
    resultDisplayDelay.setOnFinished { _ =>
      rainingBgmPlayer.stop()
      gameBgmPlayer.stop()
      ParkingFrenzy.showResult()
    }
    resultDisplayDelay.play()
  }

  // Check if the player is using drive mode or reverse mode
  private def isDriveMode : Boolean = drivingModeSlider.value() >= 50

  // Handle player key pressed input for controlling the car's movement
  private def handleInput(): Unit = {
    // Ensure the slide is always on focus for player control
    drivingModeSlider.requestFocus()

    // Steer the car to the left if key A is pressed
    if (ParkingFrenzy.keysPressed.contains(KeyCode.A)) {
      playerCar.rotate(if (isDriveMode) -playerCar.rotationStep else playerCar.rotationStep)
    }
    // Steer the car to the right if key D is pressed
    if (ParkingFrenzy.keysPressed.contains(KeyCode.D)) {
      playerCar.rotate(if (isDriveMode) playerCar.rotationStep else -playerCar.rotationStep)
    }
    // Accelerate the car if key W is pressed
    if (ParkingFrenzy.keysPressed.contains(KeyCode.W)) {
      playerCar.accelerate()
    }
    // Decelerate/stop the car if key S is pressed
    if (ParkingFrenzy.keysPressed.contains(KeyCode.S)) {
      playerCar.decelerate()
    }
    // Slow the car down to idle speed if the player does not accelerate the car
    // Move the car in idle speed if the player does not decelerate/stop the car
    if (!ParkingFrenzy.keysPressed.contains(KeyCode.W) && !ParkingFrenzy.keysPressed.contains(KeyCode.S)) {
      if (isDriveMode) playerCar.applyFriction() else playerCar.applyReverseFriction()
    }
  }

  // Update the player car's image view position based on the car's current position and rotation
  private def updatePlayerCarImageViewPosition(): Unit = {
    playerCarImageView.x = playerCar.x
    playerCarImageView.y = playerCar.y
    playerCarImageView.rotate = playerCar.rotation
  }

  // Check if the player's car has collided with any obstacles or the window boundary
  private def collided: Boolean = {
    val collidedWithCarObstacle: Boolean = currentLevel.carObstacles.exists(carObstacle => CollisionDetector.isCollidingWithCarObstacle(playerCar, carObstacle))
    val collidedWithWindow: Boolean = CollisionDetector.isCollidingWithWindow(playerCar, parkingArea.prefWidth.toDouble, parkingArea.prefHeight.toDouble)

    collidedWithCarObstacle || collidedWithWindow
  }

  // Handle the event of a car collision
  private def handleCollision(): Unit = {
    // Sound effect
    playCarCrashSound()
    // Temporarily stop the main game loop
    game.stop()
    // Reset player car position
    playerCar.resetPosition()
    // Update player car image view and resume the main game loop after 1.5s delay
    val resetPositionDelay = new PauseTransition(Duration(1500)) /* Reference: (Jewelsea, 2023) */
    resetPositionDelay.setOnFinished { _ =>
      updatePlayerCarImageViewPosition()
      game.start()
    }
    resetPositionDelay.play()
  }

  // Handle the pause and play functionality
  def handlePausePlay(): Unit = {
    pause = !pause
    if (pause){
      // Change the button image to play
      pausePlayButton.styleClass = Seq("button-play")

      stopwatch.stop()
      game.stop()
    }
    else {
      // Change the button image to pause
      pausePlayButton.styleClass = Seq("button-pause")

      stopwatch.start()
      game.start()
    }
  }

  // Display a help dialog explaining the keyboard keys involved for car control and their effects
  def handleHowToPlay(): Unit = {
    // Automatically pause the game
    pause = false
    handlePausePlay()

    // Display the help dialog
    new Alert(AlertType.Information) {
      title = "How to Play"
      headerText = "It is easy, don't panic!"
      contentText = "Key W: To accelerate the car\nKey S: To decelerate the car\nKey A: To steer the car to the left\nKey D: To steer the car to the right\nKey Up: To switch to DRIVE mode\nKey Down: To switch to REVERSE mode"
    }.showAndWait()
  }

  // On and off background music according to player selection
  def handleMusic(): Unit = {
    if (offMusicMenuItem.isSelected){
      rainingBgmPlayer.stop()
      gameBgmPlayer.stop()
    }
    else if (onMusicMenuItem.isSelected) {
      if (raining) rainingBgmPlayer.play()
      gameBgmPlayer.play()
    }
  }


  def startGame(): Unit = {
    setupParkingArea()
    setupWeather()
    // Start the main game loop
    game.start()
    // Play the game background music
    gameBgmPlayer.play()
    // Reset and start the stopwatch to track elapsed time
    stopwatch.reset()
    stopwatch.start()
    // Start the loop to update stopwatch label with elapsed time
    updateStopwatchLabel.start()
  }

  startGame()
}