package com.example.stationsapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stationsapp.R
import com.example.stationsapp.StationItem
import com.example.stationsapp.database.entities.StationEntity
import com.example.stationsapp.remote.KoleoApiService
import com.example.stationsapp.ui.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainActivityViewModel by viewModels()
    @Inject
    lateinit var apiService: KoleoApiService

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                if(lastClickedET == etStation1){
                    ///getData to station1
                }else {
                    ///getdatatostation2
                }
            }
        }

    lateinit var station1: StationEntity
    lateinit var station2: StationEntity

    lateinit var lastClickedET: EditText
    lateinit var etStation1: EditText
    lateinit var etStation2: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etStation1 = findViewById(R.id.et_station1)
        etStation2 = findViewById(R.id.et_station2)

        etStation1.setOnClickListener {
            lastClickedET = etStation1
            val intent = Intent(this@MainActivity, SearchStationActivity::class.java)
            startForResult.launch(intent)
        }

        etStation2.setOnClickListener {
            lastClickedET = etStation2
            val intent = Intent(this@MainActivity, SearchStationActivity::class.java)
            startForResult.launch(intent)
        }

        apiService.getStations().enqueue(object : Callback<List<StationItem>>{
            override fun onResponse(p0: Call<List<StationItem>>, p1: Response<List<StationItem>>) {
                Log.d("Test Koleo", p1.code().toString())
                Log.d("Test Koleo", p1.body().toString())
            }


            override fun onFailure(p0: Call<List<StationItem>>, p1: Throwable) {
                Log.e("Test Koleo", p1.toString())
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}