package com.sally.parkingfrenzy.model

import com.sally.parkingfrenzy.util.GeometryUtils

object CollisionDetector {

  // Check if the player car is within the parking lot
  def isWithinParkingLot(playerCar: PlayerCar, parkingLot: ParkingLot): Boolean = {
    val (cx, cy, cw, ch) = GeometryUtils.getRotatedBounds(playerCar.x, playerCar.y, playerCar.width, playerCar.height, playerCar.rotation)
    val (lx, ly, lw, lh) = GeometryUtils.getRotatedBounds(parkingLot.xInitial, parkingLot.yInitial, parkingLot.width, parkingLot.height, parkingLot.rotationInitial)
    // Check if the player car's bounding box is within the parking lot's bounding box
    cx >= lx && cy >= ly && cx + cw <= lx + lw && cy + ch <= ly + lh
  }

  // check if the player car is colliding with the window boundaries
  def isCollidingWithWindow(playerCar: PlayerCar, windowWidth: Double, windowHeight: Double): Boolean = {
    val (cx, cy, cw, ch) = GeometryUtils.getRotatedBounds(playerCar.x, playerCar.y, playerCar.width, playerCar.height, playerCar.rotation)
    // Check if any part of the player car's bounding box exceeds the window boundaries
    cx < 0 || cy < 0 || cx + cw > windowWidth || cy + ch > windowHeight
  }

  /* Reference: (OpenAI, 2024) */
  // Check if the player car is colliding with another car obstacle
  def isCollidingWithCarObstacle(playerCar: PlayerCar, carObstacle: CarObstacle): Boolean = {
    // Check if the two intervals overlap
    def overlap(min1: Double, max1: Double, min2: Double, max2: Double): Boolean = {
      min1 <= max2 && min2 <= max1
    }

    // Calculate the axes for a polygon based on its vertices
    def getAxes(vertices: Seq[(Double, Double)]): Seq[(Double, Double)] = {
      vertices.zip(vertices.tail :+ vertices.head).map {
        case ((x1, y1), (x2, y2)) =>
          val dx = x2 - x1
          val dy = y2 - y1
          (-dy, dx)
      }
    }

    // Get player car and car obstacle vertices
    val playerCarVertices = GeometryUtils.getPolygonVertices(playerCar.x, playerCar.y, playerCar.width, playerCar.height, playerCar.rotation)
    val carObstacleVertices = GeometryUtils.getPolygonVertices(carObstacle.xInitial, carObstacle.yInitial, carObstacle.width, carObstacle.height, carObstacle.rotationInitial)

    // Get player car and car obstacle axes
    val playerCarAxes = getAxes(playerCarVertices)
    val carObstacleAxes = getAxes(carObstacleVertices)

    // Combine the axes and remove duplicate axes
    val axes = (playerCarAxes ++ carObstacleAxes).distinct

    // Check for collisions along all axes using the Separating Axis Theorem
    axes.forall { axis =>
      val (playerCarMin, playerCarMax) = GeometryUtils.projectPolygon(playerCarVertices, axis)
      val (carObstacleMin, carObstacleMax) = GeometryUtils.projectPolygon(carObstacleVertices, axis)
      overlap(playerCarMin, playerCarMax, carObstacleMin, carObstacleMax)
    }
  }
}

