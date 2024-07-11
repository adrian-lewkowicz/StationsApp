package com.example.stationsapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.stationsapp.R
import com.example.stationsapp.StationItem
import com.example.stationsapp.database.entities.StationEntity
import com.example.stationsapp.remote.KoleoApiService
import com.example.stationsapp.ui.activities.SearchStationActivity.Companion.ID_STATION_INTENT_KEY
import com.example.stationsapp.ui.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var apiService: KoleoApiService

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if(lastClickedTV == tvStation1){
                    lifecycleScope.launch {
                        station1 = result.data?.let { mainViewModel.getStationById(it.getIntExtra(ID_STATION_INTENT_KEY, -1)) }
                        tvStation1.setText(station1?.name)
                        calculateDistance()
                    }
                }else {
                    lifecycleScope.launch {
                        station2 = result.data?.let { mainViewModel.getStationById(it.getIntExtra(ID_STATION_INTENT_KEY, -1)) }
                        tvStation2.setText(station2?.name)
                        calculateDistance()
                    }
                }
            }
        }

    var station1: StationEntity? = null
    var station2: StationEntity? = null

    lateinit var lastClickedTV: TextView
    lateinit var tvStation1: TextView
    lateinit var tvStation2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainViewModel.initializeData()

        tvStation1 = findViewById(R.id.et_station1)
        tvStation2 = findViewById(R.id.et_station2)

        tvStation1.setOnClickListener {
            lastClickedTV = tvStation1
            val intent = Intent(this@MainActivity, SearchStationActivity::class.java)
            startForResult.launch(intent)
        }

        tvStation2.setOnClickListener {
            lastClickedTV = tvStation2
            val intent = Intent(this@MainActivity, SearchStationActivity::class.java)
            startForResult.launch(intent)
        }
    }

    fun calculateDistance(){
        if(station1 != null && station2!= null){
            val distance = haversine(station1!!.latitude, station1!!.longitude, station2!!.latitude, station2!!.longitude)
            val tvDistance = findViewById<TextView>(R.id.tv_calulated_distance)
            val formattedNumber = String.format("%.2f", distance)
            val finalText = StringBuilder()
                .append(getString(R.string.distance_between_stations))
                .append(" ")
                .append(formattedNumber)
            tvDistance.setText(finalText)
        }
    }

    fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371e3
        val phi1 = lat1 * Math.PI / 180
        val phi2 = lat2 * Math.PI / 180
        val deltaPhi = (lat2 - lat1) * Math.PI / 180
        val deltaLambda = (lon2 - lon1) * Math.PI / 180

        val a = sin(deltaPhi / 2).pow(2.0) +
                cos(phi1) * cos(phi2) *
                sin(deltaLambda / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        val distance = R * c / 1000
        return distance
    }
}