package com.example.stationsapp.ui.adapters

import com.example.stationsapp.database.entities.StationKeywordsEntity

interface OnItemClick {
    fun onItemClick(keyword: StationKeywordsEntity)
}