package com.example.stationsapp

import android.content.Context
import androidx.room.Room
import com.example.stationsapp.database.AppDatabase
import com.example.stationsapp.database.dao.KeywordsDao
import com.example.stationsapp.database.dao.StationDao
import com.example.stationsapp.remote.ApiUtils
import com.example.stationsapp.remote.KoleoApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Provides
    fun provideKeywordsDao(db: AppDatabase): KeywordsDao {
        return db.keywordsDao()
    }

    @Provides
    fun provideStationDao(db: AppDatabase): StationDao{
        return db.stationDao()
    }

    @Provides
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(ApiUtils.KOLEO_STATIONS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader(ApiUtils.KOLEO_VERSION_KEY, ApiUtils.KOLEO_VERSION_VALUE)
                        .build()
                    chain.proceed(request)
                }
                .build())
            .build()
    }

    @Provides
    fun provideKoleoApiService(retrofit: Retrofit): KoleoApiService {
        return retrofit.create(KoleoApiService::class.java)
    }
}