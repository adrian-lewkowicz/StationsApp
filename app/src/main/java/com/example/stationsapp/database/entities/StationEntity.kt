package com.example.stationsapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stationsapp.StationItem
import java.util.Date


@Entity(tableName = "stations")
data class StationEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "has_announcements") val hasAnnouncements: Boolean,
    @ColumnInfo(name = "hits") val hits: Int,
    @ColumnInfo(name = "ibnr") val ibnr: Int,
    @ColumnInfo(name = "is_group") val isGroup: Boolean,
    @ColumnInfo(name = "is_nearby_station_enabled") val isNearbyStationEnabled: Boolean,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "localised_name") val localisedName: String?,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "name_slug") val nameSlug: String,
    @ColumnInfo(name = "region") val region: String,
    @ColumnInfo(name = "date") var date: Date
)

fun StationItem.toEntity(): StationEntity {
    return StationEntity(
        id = this.id,
        city = this.city,
        country = this.country,
        hasAnnouncements = this.has_announcements,
        hits = this.hits,
        ibnr = this.ibnr,
        isGroup = this.is_group,
        isNearbyStationEnabled = this.is_nearby_station_enabled,
        latitude = this.latitude,
        localisedName = this.localised_name,
        longitude = this.longitude,
        name = this.name,
        nameSlug = this.name_slug,
        region = this.region,
        date = Date()
    )
}

fun StationEntity.toItem(): StationItem {
    return StationItem(
        city = this.city,
        country = this.country,
        has_announcements = this.hasAnnouncements,
        hits = this.hits,
        ibnr = this.ibnr,
        id = this.id,
        is_group = this.isGroup,
        is_nearby_station_enabled = this.isNearbyStationEnabled,
        latitude = this.latitude,
        localised_name = this.localisedName,
        longitude = this.longitude,
        name = this.name,
        name_slug = this.nameSlug,
        region = this.region
    )
}