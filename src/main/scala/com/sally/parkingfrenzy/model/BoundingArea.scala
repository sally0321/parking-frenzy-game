package com.sally.parkingfrenzy.model

abstract class BoundingArea(val xInitial: Double,
                            val yInitial: Double,
                            val rotationInitial: Double) {
  val width: Double
  val height: Double
}

