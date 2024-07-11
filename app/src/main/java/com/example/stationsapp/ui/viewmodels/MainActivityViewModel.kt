package com.example.stationsapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.stationsapp.database.entities.StationEntity
import com.example.stationsapp.repository.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject constructor(private val repository: StationRepository) : ViewModel()  {

    suspend fun getStationById(stationId: Int): StationEntity {
        return repository.getStation(stationId)
    }

    fun initializeData(){
        repository.initializeData()
    }
}