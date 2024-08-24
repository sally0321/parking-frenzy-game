package com.sally.parkingfrenzy

import com.sally.parkingfrenzy.model.{GameRecord, PlayerCar}
import com.sally.parkingfrenzy.util.Database
import javafx.scene.layout.BorderPane
import scalafx.application.JFXApp
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.scene.image.Image
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.media.{Media, MediaPlayer}

import java.net.URL


object ParkingFrenzy extends JFXApp {

  // Initialize the database
  Database.setupDB()

  // Initialize public variables
  var selectedCar: PlayerCar = PlayerCar.taxi
  var playerName: String = ""
  var gameRecord: GameRecord = new GameRecord()
  var keysPressed: Set[KeyCode] = Set[KeyCode]()

  // Load the root layout
  private val rootResource: URL = getClass.getResource("view/RootLayout.fxml")
  private val loader: FXMLLoader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load()
  private val roots: BorderPane = loader.getRoot[jfxs.layout.BorderPane]

  // Configure the main application window
  stage = new PrimaryStage {
    title = "Parking Frenzy"
    icons += new Image(getClass.getResourceAsStream("images/logo.png"))
    minWidth = 1280
    minHeight = 720

    scene = new Scene() {
      root = roots
      stylesheets += getClass.getResource("view/Theme.css").toString

      // Track key presses and releases update the keyPressed set for car movement control
      onKeyPressed = (event: KeyEvent) => {
        keysPressed += event.code
      }
      onKeyReleased = (event: KeyEvent) => {
        keysPressed -= event.code
      }
    }
  }

  // Create a media player for general button click sound
  private val buttonClickPlayer: MediaPlayer = createMediaPlayer("audio/car-horn-sound.mp3", 1)

  // Play general button click sound effect
  def playButtonClickSound(): Unit = {
    buttonClickPlayer.stop()
    buttonClickPlayer.play()
  }

  // Load and display home page
  def showHome(): Unit = {
    val resource = getClass.getResource("view/Home.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // Load and display select car page
  def showSelectCar(): Unit = {
    val resource = getClass.getResource("view/SelectCar.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // Load and display game page
  def showGame(): Unit = {
    val resource = getClass.getResource("view/Game.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // Load and display how to play page
  def showHowToPlay(): Unit = {
    val resource = getClass.getResource("view/HowToPlay.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // Load and display leaderboard page
  def showLeaderboard(): Unit = {
    val resource = getClass.getResource("view/Leaderboard.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // Load and display result page
  def showResult(): Unit = {
    val resource = getClass.getResource("view/Result.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // Create a media player using the specified file path and cycle count
  def createMediaPlayer(filePath: String, count:Int): MediaPlayer = {
    val media: Media = new Media(getClass.getResource(filePath).toString)
    new MediaPlayer(media){
      cycleCount = count
    }
  }

  // Display home page when the application runs
  showHome()
}