package com.sally.parkingfrenzy.model

import scalafx.scene.image.Image

case class CarObstacle(_xInitial: Double,
                       _yInitial: Double,
                       _rotationInitial: Double,
                       _image: Image
                      ) extends Car(_xInitial, _yInitial, _rotationInitial, _image){}
