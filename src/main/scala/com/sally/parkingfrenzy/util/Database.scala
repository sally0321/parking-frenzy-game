package com.sally.parkingfrenzy.util

import com.sally.parkingfrenzy.model.GameRecord
import scalikejdbc._

/* Reference: (OOP Lecture, 2024)*/
trait Database {
  // JDBC driver class name for Apache Derby
  private val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"

  // Database URL for connecting to the embedded Derby database
  private val dbURL = "jdbc:derby:myDB;create=true;";

  // Initialize JDBC driver & connection pool
  Class.forName(derbyDriverClassname)
  ConnectionPool.singleton(dbURL, "parkingfrenzy", "parkingfrenzy")

  // Ad-hoc session provider on the REPL
  implicit val session = AutoSession
}

object Database extends Database{
  // Set up the database if it is not initialized yet
  def setupDB() = {
    if (!hasDBInitialize)
      GameRecord.initializeTable()
  }

  // Check if the database has been initialized
  def hasDBInitialize : Boolean = {
    DB getTable "gameRecord" match {
      case Some(x) => true
      case None => false
    }
  }
}
