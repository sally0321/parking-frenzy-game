package com.sally.parkingfrenzy.model

import scalafx.scene.image.Image

abstract class Car(_xInitial: Double,
                   _yInitial: Double,
                   _rotationInitial: Double,
                   val image: Image
                  ) extends BoundingArea(_xInitial, _yInitial, _rotationInitial) {
  val width = 60
  val height = 120
}


