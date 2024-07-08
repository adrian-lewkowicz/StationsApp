package com.example.stationsapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.stationsapp.StationItem
import com.example.stationsapp.StationKeywordsItem
import com.example.stationsapp.Utils
import com.example.stationsapp.database.dao.KeywordsDao
import com.example.stationsapp.database.dao.StationDao
import com.example.stationsapp.database.entities.StationEntity
import com.example.stationsapp.database.entities.StationKeywordsEntity
import com.example.stationsapp.database.entities.toEntity
import com.example.stationsapp.remote.KoleoApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class StationRepository @Inject constructor(private val stationDao: StationDao,
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
                loadInitialStations()
            }
        }
        scope.launch {
            val entity = keywordsDao.getOldestKeyword()
            if(entity != null){
                if(Utils.checkIfIsCanBeUpdated(entity.date)){
                    updateKeywords()
                }
            }else{
                loadInitialKeywords()
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

    private suspend fun loadInitialStations(){
        ///todo loading initial data
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

    private suspend fun loadInitialKeywords(){
        ///todo loading initial data
    }
}