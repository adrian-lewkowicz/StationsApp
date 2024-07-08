package com.example.stationsapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.stationsapp.database.entities.StationEntity

@Dao
interface StationDao {
    @Insert
    suspend fun insert(station: StationEntity)

    @Insert
    suspend fun insertAll(stationList: List<StationEntity>)

    @Query("SELECT * FROM stations WHERE id = :stationId")
    suspend fun getStationById(stationId: Int): StationEntity

    @Query("SELECT * FROM stations ORDER BY date ASC LIMIT 1")
    suspend fun getOldestStation(): StationEntity?

    @Query("DELETE FROM stations")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAllStations(stationList: List<StationEntity>){
        deleteAll()
        insertAll(stationList)
    }
}