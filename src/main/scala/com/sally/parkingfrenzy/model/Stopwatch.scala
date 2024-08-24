package com.sally.parkingfrenzy.model

class Stopwatch {
  private var startTime: Long = 0
  private var endTime: Long = 0
  private var elapsedTime: Long = 0
  private var running: Boolean = false

  // Start the stopwatch and record the start time
  def start(): Unit = {
    if (!running){
      startTime = System.currentTimeMillis()
      running = true
    }
  }

  // Stop the stopwatch and update the elapsed time
  def stop(): Unit = {
    if (running) {
      endTime = System.currentTimeMillis()
      elapsedTime += endTime - startTime
      running = false
    }
  }

  // Calculate and return the elapsed time
  def getElapsedTime: Long = {
    if (running) {
      elapsedTime + System.currentTimeMillis() - startTime
    } else {
      elapsedTime
    }
  }

  // Reset the stopwatch
  def reset(): Unit = {
    startTime = 0
    endTime = 0
    elapsedTime = 0
    running = false
  }
}

