package com.example.stationsapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stationsapp.R
import com.example.stationsapp.database.entities.StationKeywordsEntity
import com.example.stationsapp.ui.adapters.OnItemClick
import com.example.stationsapp.ui.adapters.StationAdapter
import com.example.stationsapp.ui.viewmodels.SearchingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchStationActivity : AppCompatActivity() {
    companion object{
        const val ID_STATION_INTENT_KEY="ID_STATION_KEY"
    }

    private val stationViewModel: SearchingViewModel by viewModels()
    private lateinit var stationAdapter: StationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_station)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchView = findViewById<SearchView>(R.id.searchView)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        stationAdapter = StationAdapter(object: OnItemClick{
            override fun onItemClick(keyword: StationKeywordsEntity) {
                val returnIntent = Intent()
                returnIntent.putExtra(ID_STATION_INTENT_KEY, keyword.stationId);
                setResult(RESULT_OK, returnIntent)
                finish()
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = stationAdapter

        stationViewModel.searchResults.observe(this, { stations ->
            stationAdapter.submitList(stations)
            stationAdapter.notifyDataSetChanged()
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    stationViewModel.searchStation(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    stationViewModel.searchStation(newText)
                }
                return true
            }
        })
    }
}