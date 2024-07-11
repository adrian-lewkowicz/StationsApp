package com.example.stationsapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.stationsapp.StationKeywordsItem
import java.util.Date

@Entity(tableName = "station_keywords",
    foreignKeys = [ForeignKey(
        entity = StationEntity::class,
        parentColumns = ["id"],
        childColumns = ["station_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class StationKeywordsEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "keyword") val keyword: String,
    @ColumnInfo(name = "station_id", index = true) val stationId: Int,
    @ColumnInfo(name = "date") var date: Date
)
    fun StationKeywordsItem.toEntity(): StationKeywordsEntity {
        return StationKeywordsEntity(
            id = this.id,
            keyword = this.keyword,
            stationId = this.station_id,
            date = Date()
        )
    }

    fun StationKeywordsEntity.toItem(): StationKeywordsItem {
        return StationKeywordsItem(
            id = this.id,
            keyword = this.keyword,
            station_id = this.stationId
        )
    }
