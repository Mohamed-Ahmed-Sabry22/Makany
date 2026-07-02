package com.kabo.a24_makany.ui.screens.places

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kabo.a24_makany.data.local.PlaceEntity
import com.kabo.a24_makany.data.local.PlacesDatabase
import com.kabo.a24_makany.data.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlacesViewModel(
    application: Application
) : AndroidViewModel(application){
    //خاص ب Roooooom
    private val repo : PlacesRepository

    //val places : Flow<List<PlaceEntity>>

    private val _uiState = MutableStateFlow(PlacesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val dao = PlacesDatabase.getInstance(application).dao

        repo = PlacesRepository(dao)

        viewModelScope.launch {
            repo.getAllPlaces().collect { places ->

                _uiState.value = PlacesUiState(
                    isLoading = false,
                    places = places
                )

            }
        }
    }
    fun savePlace(place: PlaceEntity) {
        viewModelScope.launch {
            repo.upsert(place)
        }
    }

    fun deletePlace(place: PlaceEntity) {
        viewModelScope.launch {
            repo.delete(place)
        }
    }
}