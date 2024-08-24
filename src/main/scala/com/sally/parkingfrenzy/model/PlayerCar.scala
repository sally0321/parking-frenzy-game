package com.sally.parkingfrenzy.model

import scalafx.scene.image.Image

class PlayerCar(private var _x: Double,
                private var _y: Double,
                private var _rotation: Double,
                _image: Image,
                val maxSpeed: Double,
                val initialAccelerationRate: Double,
                val initialDecelerationRate: Double,
                val carType: String) extends Car(_x, _y, _rotation, _image){

  // Car speed will be positive in drive mode and negative in reverse mode
  private var _currentSpeed: Double = 0.0
  val idleSpeed: Double = 0.2

  private var _accelerationRate: Double = initialAccelerationRate
  private var _decelerationRate: Double = initialDecelerationRate

  val initialFriction: Double = 0.05
  private var _friction: Double = initialFriction

  // The amount of car rotation each time when player try to steer the car to the left or right
  val rotationStep: Double = 0.45

  // Getter
  def x: Double = _x
  def y: Double = _y
  def rotation: Double = _rotation
  def currentSpeed: Double = _currentSpeed
  def accelerationRate: Double = _accelerationRate
  def decelerationRate: Double = _decelerationRate
  def friction: Double = _friction

  // Rotate the car in specified angle
  def rotate(angle: Double): Unit = {
    _rotation += angle
  }

  /* Reference: (OpenAI, 2024) */
  // Move and update the car position
  def move(): Unit = {
    val angleInRadians = Math.toRadians(rotation)
    _x += Math.sin(angleInRadians) * currentSpeed
    _y -= Math.cos(angleInRadians) * currentSpeed
  }

  // Accelerate the car
  def accelerate(): Unit = {
    // Accelerate the car when it is in drive mode
    if (currentSpeed > 0) {
      _currentSpeed = Math.min(currentSpeed + accelerationRate, maxSpeed)
    }
    // Accelerate the car when it is in reverse mode
    else{
      _currentSpeed = Math.max(currentSpeed - accelerationRate, -maxSpeed)
    }
  }

  // Decelerate the car
  def decelerate(): Unit = {
    // Decelerate the car when it is in drive mode
    if (currentSpeed > 0) {
      _currentSpeed = Math.max(currentSpeed - decelerationRate, 0)
    }
    // Decelerate the car when it is in reverse mode
    else {
      _currentSpeed = Math.min(currentSpeed + decelerationRate, 0)
    }
  }

  // Slow down the car to idle speed when the car is in drive mode
  def applyFriction(): Unit = {
    _currentSpeed = Math.max(currentSpeed - friction, idleSpeed)
  }

  // Slow down the car to idle speed when the car is in reverse mode
  def applyReverseFriction(): Unit = {
    _currentSpeed = Math.min(currentSpeed + friction, -idleSpeed)
  }

  // Reset the position of the car
  def resetPosition(): Unit = {
    _x = xInitial
    _y = yInitial
    _rotation = rotationInitial
    _currentSpeed = 0
  }

  // Reset the characteristics of the car to its initial value or according to the effect of the raining condition
  def resetCharacteristics(raining: Boolean): Unit = {
    if (raining){
      _accelerationRate = initialAccelerationRate + 0.03
      _decelerationRate = initialDecelerationRate - 0.03
      _friction = initialFriction - 0.02
    }
    else{
      _accelerationRate = initialAccelerationRate
      _decelerationRate = initialDecelerationRate
      _friction = initialFriction
    }
  }
}

object PlayerCar{
  val sportCar = new PlayerCar(100, 50, 90, new Image(getClass.getResourceAsStream("../images/player-sport-car.png")), 5, 0.2, 0.2, "Sport Car")
  val policeCar = new PlayerCar(100, 50, 90, new Image(getClass.getResourceAsStream("../images/player-police-car.png")), 4, 0.15, 0.15, "Police Car")
  val taxi = new PlayerCar(100, 50, 90, new Image(getClass.getResourceAsStream("../images/player-taxi.png")), 3, 0.1, 0.1, "Taxi")
}
