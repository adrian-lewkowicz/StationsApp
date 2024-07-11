package com.example.stationsapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stationsapp.R
import com.example.stationsapp.database.entities.StationKeywordsEntity

class StationAdapter(private val listener: OnItemClick) : RecyclerView.Adapter<StationAdapter.StationViewHolder>() {



    private var stationList = listOf<StationKeywordsEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.station_name, parent, false)
        return StationViewHolder(view, listener)
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

    inner class StationViewHolder(itemView: View,
                            private val listener: OnItemClick) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_stationName)

        init {
            itemView.setOnClickListener {
                if(adapterPosition != RecyclerView.NO_POSITION){
                    listener.onItemClick(stationList.get(adapterPosition))
                }
            }
        }

        fun bind(station: StationKeywordsEntity) {
            nameTextView.text = station.keyword
        }
    }
}