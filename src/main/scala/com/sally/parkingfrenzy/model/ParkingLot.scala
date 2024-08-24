package com.sally.parkingfrenzy.model

case class ParkingLot(_xInitial: Double,
                      _yInitial: Double,
                      _rotationInitial: Double
                     ) extends BoundingArea(_xInitial, _yInitial, _rotationInitial) {
  val width = 80
  val height = 160
}
