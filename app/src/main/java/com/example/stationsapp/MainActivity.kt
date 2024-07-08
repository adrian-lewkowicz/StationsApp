package com.example.stationsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stationsapp.remote.KoleoApiService
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var apiService: KoleoApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        apiService.getStations().enqueue(object : Callback<StationResponse>{
            override fun onResponse(p0: Call<StationResponse>, p1: Response<StationResponse>) {
                Log.d("Test Koleo", p1.code().toString())
                Log.d("Test Koleo", p1.body().toString())
            }

            override fun onFailure(p0: Call<StationResponse>, p1: Throwable) {
                Log.e("Test Koleo", p1.toString())
            }
        })
    }
}