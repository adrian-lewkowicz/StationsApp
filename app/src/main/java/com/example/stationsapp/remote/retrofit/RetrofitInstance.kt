package com.example.stationsapp.remote.retrofit

import com.example.stationsapp.remote.ApiUtils
import com.example.stationsapp.remote.KoleoApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiUtils.KOLEO_STATIONS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader(ApiUtils.KOLEO_VERSION_KEY, ApiUtils.KOLEO_VERSION_VALUE)
                        .build()
                    chain.proceed(request)
                }
                .build())
            .build()
    }

    val api: KoleoApiService by lazy {
        retrofit.create(KoleoApiService::class.java)
    }
}