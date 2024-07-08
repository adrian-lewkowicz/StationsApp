package com.example.stationsapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.stationsapp.database.entities.StationKeywordsEntity

@Dao
interface KeywordsDao {
    @Insert
    suspend fun insert(keyword: StationKeywordsEntity)

    @Query("SELECT * FROM station_keywords")
    suspend fun getAllKeywords(): List<StationKeywordsEntity>

    @Query("SELECT * FROM station_keywords ORDER BY date ASC LIMIT 1")
    suspend fun getOldestKeyword(): StationKeywordsEntity?
}