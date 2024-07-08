package com.example.stationsapp

import com.example.stationsapp.database.entities.StationKeywordsEntity
import java.util.Date

data class StationKeywordsItem(
    val id: Int,
    val keyword: String,
    val station_id: Int
){
    fun toEntity(): StationKeywordsEntity {
        return StationKeywordsEntity(
            id = this.id,
            keyword = this.keyword,
            stationId = this.station_id,
            date = Date()
        )
    }
}