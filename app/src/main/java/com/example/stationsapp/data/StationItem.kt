package com.example.stationsapp

data class StationResponseItem(
    val city: String,
    val country: String,
    val has_announcements: Boolean,
    val hits: Int,
    val ibnr: Int,
    val id: Int,
    val is_group: Boolean,
    val is_nearby_station_enabled: Boolean,
    val latitude: Double,
    val localised_name: String,
    val longitude: Double,
    val name: String,
    val name_slug: String,
    val region: String
)