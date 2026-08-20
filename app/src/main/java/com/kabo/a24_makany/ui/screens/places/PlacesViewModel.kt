package com.kabo.a24_makany.ui.screens.places

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kabo.a24_makany.data.local.PlaceEntity
import com.kabo.a24_makany.data.local.PlacesDatabase
import com.kabo.a24_makany.data.repository.PlacesRepository
import com.kabo.a24_makany.usecase.IsPlaceNameTakenUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlacesViewModel(
    private val repo: PlacesRepository,
    private val isPlaceNameTakenUseCase: IsPlaceNameTakenUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlacesUiState())
    val uiState = _uiState.asStateFlow()

    init {
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
            if (isPlaceNameTakenUseCase(place.name)) {
                    // lesa
            } else {
                repo.upsert(place)
            }
        }
    }

    fun deletePlace(place: PlaceEntity) {
        viewModelScope.launch {
            repo.delete(place)
        }
    }
}