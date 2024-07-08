package com.example.stationsapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.stationsapp.database.entities.StationEntity

@Dao
interface StationDao {
    @Insert
    suspend fun insert(station: StationEntity)

    @Query("SELECT * FROM stations")
    suspend fun getAllStations(): List<StationEntity>

    @Query("SELECT * FROM stations ORDER BY date ASC LIMIT 1")
    suspend fun getOldestStation(): StationEntity?
}