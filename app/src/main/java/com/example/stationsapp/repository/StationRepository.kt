package com.example.stationsapp.repository

import com.example.stationsapp.database.dao.KeywordsDao
import com.example.stationsapp.database.dao.StationDao
import com.example.stationsapp.remote.KoleoApiService
import javax.inject.Inject

class StationRepository @Inject constructor(private val stationDao: StationDao,
                                            private val keywordsDao: KeywordsDao,
                                            private val koleoApiService: KoleoApiService) {



}