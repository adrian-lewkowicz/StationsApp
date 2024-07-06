package com.example.stationsapp.remote

import com.example.stationsapp.StationKeywordsResponse
import com.example.stationsapp.StationResponse
import retrofit2.Call
import retrofit2.http.GET

interface KoleoApiService {
    @GET("stations")
    fun getStations(): Call<StationResponse>

    @GET("station_keywords")
    fun getKeywordsToStations(): Call<StationKeywordsResponse>
}