package com.example.stationsapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.stationsapp.database.entities.StationKeywordsEntity
import com.example.stationsapp.repository.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchingViewModel
@Inject constructor(private val repository: StationRepository) : ViewModel() {

    private val _searchResulsts = MutableLiveData<List<StationKeywordsEntity>>()
    val searchResults: LiveData<List<StationKeywordsEntity>> = _searchResulsts

    init {
        viewModelScope.launch {
            val keywords = repository.getKeywords()
            _searchResulsts.postValue(keywords)
        }
    }

    fun searchStation(query: String){
        ///todo normalize query
        viewModelScope.launch {
            _searchResulsts.postValue(repository.searchStation(query))
        }
    }

}