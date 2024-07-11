package com.example.stationsapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.stationsapp.R
import com.example.stationsapp.database.entities.StationEntity
import com.example.stationsapp.remote.KoleoApiService
import com.example.stationsapp.ui.activities.SearchStationActivity.Companion.ID_STATION_INTENT_KEY
import com.example.stationsapp.ui.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
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
                        marker1?.let {
                            mapView.overlays.remove(it)
                            mapView.invalidate()
                        }
                        val point1 =
                            station1?.let { GeoPoint(it.latitude, it.longitude) }
                        marker1 = Marker(mapView).apply {
                            position = point1
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        }
                        mapView.overlays.add(marker1)
                        val startPoint = station1?.let { GeoPoint(it.latitude, it.longitude) }
                        mapView.controller.setCenter(startPoint)
                        calculateDistance()
                    }
                }else {
                    lifecycleScope.launch {
                        station2 = result.data?.let { mainViewModel.getStationById(it.getIntExtra(ID_STATION_INTENT_KEY, -1)) }
                        tvStation2.setText(station2?.name)
                        marker2?.let {
                            mapView.overlays.remove(it)
                            mapView.invalidate()
                        }
                        val point2 =
                            station2?.let { GeoPoint(it.latitude, it.longitude) }
                        marker2 = Marker(mapView).apply {
                            position = point2
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        }
                        mapView.overlays.add(marker2)
                        calculateDistance()
                    }
                }
            }
        }

    var station1: StationEntity? = null
    var station2: StationEntity? = null
    private var marker1: Marker? = null
    private var marker2: Marker? = null

    lateinit var lastClickedTV: TextView
    lateinit var tvStation1: TextView
    lateinit var tvStation2: TextView
    lateinit var mapView: MapView

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

        Configuration.getInstance().setUserAgentValue("com.example.stationsapp");

        mapView = findViewById(R.id.mapview);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);
        val startPoint = GeoPoint(52.2297, 21.0122)
        mapView.controller.setCenter(startPoint)
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

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
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
            val boundingBox = BoundingBox(
                maxOf(station1!!.latitude, station2!!.latitude) + 0.1,
                maxOf(station1!!.longitude, station2!!.longitude) + 0.1,
                minOf(station1!!.latitude, station2!!.latitude) - 0.1,
                minOf(station1!!.longitude, station2!!.longitude) - 0.1
            )
            mapView.zoomToBoundingBox(boundingBox, true)
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