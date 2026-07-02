package com.kabo.a24_makany.ui.screens.places

import com.kabo.a24_makany.data.local.PlaceEntity

data class PlacesUiState(
    val isLoading: Boolean = true,
    val places: List<PlaceEntity> = emptyList()
)