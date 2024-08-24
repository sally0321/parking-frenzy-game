package com.sally.parkingfrenzy.model.level

import com.sally.parkingfrenzy.model.{CarObstacle, ParkingLot}

object Level1 extends Level{

  // Set the parking lot for level 1
  val parkingLot: ParkingLot = ParkingLot(540, 280, 90)

  // Set the car obstacles for level 1
  val carObstacles: List[CarObstacle] = List(
    CarObstacle(30, 150, 0, randomCarImage),
    CarObstacle(30, 290, 0, randomCarImage),
    CarObstacle(30, 430, 0, randomCarImage),
    CarObstacle(30, 570, 0, randomCarImage),

    CarObstacle(280, 50, 0, randomCarImage),
    CarObstacle(360, 50, 0, randomCarImage),
    CarObstacle(440, 50, 0, randomCarImage),
    CarObstacle(520, 50, 0, randomCarImage),
    CarObstacle(600, 50, 0, randomCarImage),
    CarObstacle(680, 50, 0, randomCarImage),
    CarObstacle(760, 50, 0, randomCarImage),
    CarObstacle(840, 50, 0, randomCarImage),

    CarObstacle(280, 550, 0, randomCarImage),
    CarObstacle(360, 550, 0, randomCarImage),
    CarObstacle(440, 550, 0, randomCarImage),
    CarObstacle(520, 550, 0, randomCarImage),
    CarObstacle(600, 550, 0, randomCarImage),
    CarObstacle(680, 550, 0, randomCarImage),
    CarObstacle(760, 550, 0, randomCarImage),
    CarObstacle(840, 550, 0, randomCarImage),

    CarObstacle(250, 300, 90, randomCarImage),
    CarObstacle(390, 300, 90, randomCarImage),
    CarObstacle(730, 300, 90, randomCarImage),

    CarObstacle(1100, 140, 90, randomCarImage),
    CarObstacle(1100, 220, 90, randomCarImage),
    CarObstacle(1100, 300, 90, randomCarImage),
    CarObstacle(1100, 380, 90, randomCarImage),
    CarObstacle(1100, 460, 90, randomCarImage),
    CarObstacle(1100, 540, 90, randomCarImage)
  )
}
