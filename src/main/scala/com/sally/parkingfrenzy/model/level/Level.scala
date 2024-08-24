package com.sally.parkingfrenzy.model.level

import com.sally.parkingfrenzy.model.{CarObstacle, ParkingLot}
import scalafx.scene.image.Image

trait Level {
  // A parking lot for the level
  val parkingLot : ParkingLot
  // A list of car obstacles present in the level
  val carObstacles: List[CarObstacle]

  // List of file paths for car obstacle images
  private val carImagesList: List[String] = List(
    "../../images/car-obstacle-beige.png",
    "../../images/car-obstacle-brown.png",
    "../../images/car-obstacle-light-brown.png",
    "../../images/car-obstacle-blue.png",
    "../../images/car-obstacle-green.png",
    "../../images/car-obstacle-light-beige.png")

  // Return a random image for a car obstacle from the car images list
  def randomCarImage: Image ={
    val randomIndex = (math.random() * carImagesList.length).toInt
    new Image(getClass.getResourceAsStream(carImagesList(randomIndex)))
  }
}
