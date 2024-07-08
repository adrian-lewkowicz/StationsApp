package com.example.stationsapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.stationsapp.database.converters.DateTypeConverter
import com.example.stationsapp.database.dao.StationDao
import com.example.stationsapp.database.entities.StationEntity
import com.example.stationsapp.database.entities.StationKeywordsEntity

@Database(entities = [StationEntity::class, StationKeywordsEntity::class], version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
}