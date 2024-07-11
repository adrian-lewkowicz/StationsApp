package com.example.stationsapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.stationsapp.database.entities.StationKeywordsEntity

@Dao
interface KeywordsDao {
    @Insert
    suspend fun insert(keyword: StationKeywordsEntity)

    @Insert
    suspend fun insertAll(keywordList: List<StationKeywordsEntity>)

    @Query("SELECT station_keywords.* FROM station_keywords " +
            "INNER JOIN stations ON station_keywords.station_id = stations.id " +
            "ORDER BY stations.hits DESC")
    suspend fun getAllKeywords(): List<StationKeywordsEntity>

    @Query("SELECT * FROM station_keywords ORDER BY date ASC LIMIT 1")
    suspend fun getOldestKeyword(): StationKeywordsEntity?

    @Query("SELECT station_keywords.* FROM station_keywords " +
            "INNER JOIN stations ON station_keywords.station_id = stations.id " +
            "WHERE station_keywords.clean_keyword LIKE '%' || LOWER(:query) || '%' " +
            "ORDER BY stations.hits DESC")
    suspend fun searchKeywords(query: String): List<StationKeywordsEntity>

    @Query("DELETE from station_keywords")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(keywordList: List<StationKeywordsEntity>){
        deleteAll()
        insertAll(keywordList)
    }
}