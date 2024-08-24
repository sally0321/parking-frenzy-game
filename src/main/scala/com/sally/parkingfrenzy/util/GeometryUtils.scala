package com.sally.parkingfrenzy.util

/* Reference: (OpenAI, 2024; Collision Detector, 2017) */
object GeometryUtils {
  // Calculate the vertices of a rotated rectangle
  def getPolygonVertices(x: Double, y: Double, width: Double, height: Double, rotation: Double): Seq[(Double, Double)] = {
    val theta = Math.toRadians(rotation)
    val (cx, cy) = (x + width / 2, y + height / 2)

    // Define the four corners of the rectangle before rotation
    val corners = Seq(
      (x, y),
      (x + width, y),
      (x + width, y + height),
      (x, y + height)
    )

    // Rotate each corner around the center and return the new vertices
    corners.map { case (px, py) => rotatePoint(px, py, cx, cy, theta) }
  }

  // Project the vertices of a polygon onto a specified axis and returns the min and max projections
  def projectPolygon(vertices: Seq[(Double, Double)], axis: (Double, Double)): (Double, Double) = {
    // Calculate the dot product of each vertex with the axis
    val projections = vertices.map { case (px, py) => px * axis._1 + py * axis._2 }
    (projections.min, projections.max)
  }

  // Rotate a point (px, py) around a center (cx, cy) by a given angle theta
  def rotatePoint(px: Double, py: Double, cx: Double, cy: Double, theta: Double): (Double, Double) = {
    val cosTheta = Math.cos(theta)
    val sinTheta = Math.sin(theta)
    val dx = px - cx
    val dy = py - cy
    (
      cx + dx * cosTheta - dy * sinTheta,
      cy + dx * sinTheta + dy * cosTheta
    )
  }

  // Calculate the bounding box of a rotated rectangle
  def getRotatedBounds(x: Double, y: Double, width: Double, height: Double, rotation: Double): (Double, Double, Double, Double) = {
    val theta = Math.toRadians(rotation)
    val (cx, cy) = (x + width / 2, y + height / 2)

    // Get the four corners of the rectangle
    val corners = Seq(
      (x, y),
      (x + width, y),
      (x + width, y + height),
      (x, y + height)
    )

    // Rotate each corner
    val rotatedCorners = corners.map { case (px, py) => rotatePoint(px, py, cx, cy, theta) }

    // Get the bounding box of the rotated rectangle
    val xs = rotatedCorners.map(_._1)
    val ys = rotatedCorners.map(_._2)
    (xs.min, ys.min, xs.max - xs.min, ys.max - ys.min)
  }
}
