package com.example.stationsapp.remote

import com.example.stationsapp.StationItem
import com.example.stationsapp.StationKeywordsItem
import retrofit2.Call
import retrofit2.http.GET

interface KoleoApiService {
    @GET("stations")
    fun getStations(): Call<List<StationItem>>

    @GET("station_keywords")
    fun getKeywordsToStations(): Call<List<StationKeywordsItem>>
}