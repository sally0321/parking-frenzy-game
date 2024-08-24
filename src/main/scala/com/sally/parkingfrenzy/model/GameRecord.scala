package com.sally.parkingfrenzy.model

import com.sally.parkingfrenzy.ParkingFrenzy.{playerName, selectedCar}
import com.sally.parkingfrenzy.util.Database
import com.sally.parkingfrenzy.util.DateTimeUtils.formatDateTime
import scalafx.beans.property.{LongProperty, StringProperty}
import scalikejdbc._
import java.time.LocalDateTime
import scala.util.Try

case class GameRecord(_dateTime: String, _name: String, _car: String, _timeTaken: Long) {

  // Constructor
  def this(timeTaken: Long) = this(
    formatDateTime(LocalDateTime.now()),
    playerName,
    selectedCar.carType,
    timeTaken
  )

  // Constructor
  def this() = this(
    formatDateTime(LocalDateTime.now()),
    playerName,
    selectedCar.carType,
    0
  )

  val dateTime = new StringProperty(_dateTime)
  val name = new StringProperty(_name)
  val car = new StringProperty(_car)
  val timeTaken = new LongProperty(this, "timeTaken", _timeTaken)

  // To save the game record into the table
  def save(): Try[Int] = {
    Try(DB autoCommit { implicit session =>
      sql"""
        insert into gameRecord(
          dateTime,
          playerName,
          selectedCar,
          timeTaken)
        values(
          ${dateTime.value},
          ${name.value},
          ${car.value},
          ${timeTaken.value})
        """.update.apply()
    }).recover({
      case e: Exception =>
        println(s"Failed to save record: ${e.getMessage}")
        e.printStackTrace()
        -1
    })
  }
}

object GameRecord extends Database {
  // To create the table
  def initializeTable(): Boolean = {
    DB autoCommit { implicit session =>
      sql"""
      create table gameRecord (
        dateTime varchar(30),
        playerName varchar(128),
        selectedCar varchar(100),
        timeTaken bigint
      )
      """.execute.apply()
    }
  }

  // To retrieve the a specified number of records from all records arranged according to time taken
  def getGameRecords(count: Int): List[GameRecord] = {
    DB readOnly { implicit session =>
      sql"""
      SELECT * FROM gameRecord
      ORDER BY timeTaken
      fetch first ${count} rows only
      """.map { rs =>
        new GameRecord(
          rs.string("dateTime"),
          rs.string("playerName"),
          rs.string("selectedCar"),
          rs.long("timeTaken")
        )
      }.list.apply()
    }
  }

  // To retrieve all records arranged according to time taken
  def getAllGameRecords(): List[GameRecord] = {
    DB readOnly { implicit session =>
      sql"""
      SELECT * FROM gameRecord
      ORDER BY timeTaken
      """.map { rs =>
        new GameRecord(
          rs.string("dateTime"),
          rs.string("playerName"),
          rs.string("selectedCar"),
          rs.long("timeTaken")
        )
      }.list.apply()
    }
  }

  def getPlayerRank(gameRecord: GameRecord): Int = {
    // Retrieve all game records
    val allGameRecords = getAllGameRecords()

    // Find the rank of the new record
    val rank = allGameRecords.indexOf(gameRecord) + 1
    rank
  }
}
