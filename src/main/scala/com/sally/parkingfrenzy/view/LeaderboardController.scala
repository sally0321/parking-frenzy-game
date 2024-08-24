package com.sally.parkingfrenzy.view

import com.sally.parkingfrenzy.ParkingFrenzy
import com.sally.parkingfrenzy.model.GameRecord
import com.sally.parkingfrenzy.model.GameRecord.getGameRecords
import com.sally.parkingfrenzy.util.DateTimeUtils.formatElapsedTime
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{TableColumn, TableView}
import scalafxml.core.macros.sfxml

@sfxml
class LeaderboardController (private val gameRecordsTable: TableView[GameRecord],
                             private val rankColumn: TableColumn[GameRecord, String],
                             private val nameColumn: TableColumn[GameRecord, String],
                             private val selectedCarColumn: TableColumn[GameRecord, String],
                             private val timeTakenColumn: TableColumn[GameRecord, String],
                             private val savedAtColumn: TableColumn[GameRecord, String]){

  // Create and populate the gameRecords buffer with the top 10 game records
  private val gameRecords = new ObservableBuffer[GameRecord]()
  gameRecords ++=  getGameRecords(10)
  // Set the game records as the items in the TableView
  gameRecordsTable.items = gameRecords

  // Set up cell value factory for each column to display relevant information
  rankColumn.cellValueFactory = { record =>
    val rank = gameRecords.indexOf(record.value) + 1
    new StringProperty(rank.toString)
  }
  nameColumn.cellValueFactory = {_.value.name}
  selectedCarColumn.cellValueFactory = {_.value.car}
  timeTakenColumn.cellValueFactory =  (record) =>{
    new StringProperty(formatElapsedTime(record.value.timeTaken.value))
  }
  savedAtColumn.cellValueFactory = {_.value.dateTime}

  // Bring player back to home page when the back button is clicked
  def handleBack(): Unit = {
    ParkingFrenzy.playButtonClickSound()
    ParkingFrenzy.showHome()
  }
}
