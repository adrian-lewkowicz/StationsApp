package com.example.stationsapp

import android.content.Context
import androidx.room.Room
import com.example.stationsapp.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase{
        return Room.databaseBuilder(appContext,
            AppDatabase::class.java,
            "station_database").build()
    }



}