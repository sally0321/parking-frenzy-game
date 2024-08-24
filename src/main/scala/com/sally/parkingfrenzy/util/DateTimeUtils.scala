package com.sally.parkingfrenzy.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {
  private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

  // Format LocatDateTime into dd/MM/yyyy HH:mm:ss format
  def formatDateTime(dateTime: LocalDateTime): String = {
    DATE_TIME_FORMATTER.format(dateTime)
  }

  // Format the elapsed time from millisecond to minutes:seconds format
  def formatElapsedTime(milliseconds: Long): String = {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    // Format with leading zeros for single-digit minutes or seconds
    f"$minutes%02d:$seconds%02d"
  }
}
