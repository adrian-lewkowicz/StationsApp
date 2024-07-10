package com.example.stationsapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stationsapp.R
import com.example.stationsapp.database.entities.StationKeywordsEntity

class StationAdapter : RecyclerView.Adapter<StationAdapter.StationViewHolder>() {

    private var stationList = listOf<StationKeywordsEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.station_name, parent, false)
        return StationViewHolder(view)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val station = stationList[position]
        holder.bind(station)
    }

    override fun getItemCount() = stationList.size

    fun submitList(list: List<StationKeywordsEntity>) {
        stationList = list
        notifyDataSetChanged()
    }

    class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_stationName)

        fun bind(station: StationKeywordsEntity) {
            nameTextView.text = station.keyword
        }
    }
}