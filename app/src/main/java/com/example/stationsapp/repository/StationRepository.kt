package com.example.stationsapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.stationsapp.R
import com.example.stationsapp.StationItem
import com.example.stationsapp.StationKeywordsItem
import com.example.stationsapp.Utils
import com.example.stationsapp.database.dao.KeywordsDao
import com.example.stationsapp.database.dao.StationDao
import com.example.stationsapp.database.entities.StationEntity
import com.example.stationsapp.database.entities.StationKeywordsEntity
import com.example.stationsapp.database.entities.toEntity
import com.example.stationsapp.remote.KoleoApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class StationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stationDao: StationDao,
    private val keywordsDao: KeywordsDao,
    private val koleoApiService: KoleoApiService) {
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            val entity = stationDao.getOldestStation()
            if(entity != null){
                if(Utils.checkIfIsCanBeUpdated(entity.date)){
                    updateStations()
                }
            }else{
                loadInitialStations(context)
            }
        }
        scope.launch {
            val entity = keywordsDao.getOldestKeyword()
            if(entity != null){
                if(Utils.checkIfIsCanBeUpdated(entity.date)){
                    updateKeywords()
                }
            }else{
                loadInitialKeywords(context)
            }
        }
    }

    fun getKeywords():LiveData<List<StationKeywordsEntity>>{
        return keywordsDao.getAllKeywords()
    }

    suspend fun getStation(stationId: Int): StationEntity{
        return stationDao.getStationById(stationId)
    }

    private fun updateStations(){
        koleoApiService.getStations().enqueue(object : Callback<List<StationItem>> {
            override fun onResponse(call: Call<List<StationItem>>, response: Response<List<StationItem>>) {
                Log.d("Test Koleo", response.code().toString())
                var remoteStations = response.body()
                val stationEntities = remoteStations!!.map { it.toEntity() }
                scope.launch(Dispatchers.IO) {
                    stationDao.replaceAllStations(stationEntities)
                }
            }

            override fun onFailure(call: Call<List<StationItem>>, throwable: Throwable) {
                Log.w("Fetch failed", throwable.toString())
            }
        })
    }

    private suspend fun loadInitialStations(context: Context){
        val resources = context.resources
        val bufferedReader = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.station_response)))
        var jsonString = bufferedReader.use { it.readText() }
        val type = object : TypeToken<List<StationItem>>() {}.type
        var listItem :List<StationItem> =  Gson().fromJson(jsonString, type)
        stationDao.insertAll(listItem.map { it.toEntity() })
    }

    private fun updateKeywords(){
        koleoApiService.getKeywordsToStations().enqueue(object : Callback<List<StationKeywordsItem>> {
            override fun onResponse(call: Call<List<StationKeywordsItem>>, response: Response<List<StationKeywordsItem>>) {
                Log.d("Keywords response code:", response.code().toString())
                val remoteKeywords = response.body()
                val keywordsEntities = remoteKeywords!!.map { it.toEntity() }
                scope.launch(Dispatchers.IO) {
                    keywordsDao.replaceAll(keywordsEntities)
                }
            }

            override fun onFailure(call: Call<List<StationKeywordsItem>>, throwable: Throwable) {
                Log.w("Fetch failed", throwable.toString())
            }
        })
    }

    private suspend fun loadInitialKeywords(context: Context){
        val resources = context.resources
        val bufferedReader = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.station_keywords_response)))
        var jsonString = bufferedReader.use { it.readText() }
        val type = object : TypeToken<List<StationKeywordsItem>>() {}.type
        var listItem :List<StationKeywordsItem> =  Gson().fromJson(jsonString, type)
        keywordsDao.insertAll(listItem.map { it.toEntity() })
    }
}