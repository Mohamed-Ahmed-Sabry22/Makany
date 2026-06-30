package com.kabo.a24_makany.data.repository

import com.kabo.a24_makany.data.local.PlaceEntity
import com.kabo.a24_makany.data.local.PlacesDao

class PlacesRepository (val dao : PlacesDao) {

    suspend fun upsert (place: PlaceEntity) = dao.upsertPlace(place)

    suspend fun delete(place: PlaceEntity) = dao.deletePlace(place)

    fun getAllPlaces() = dao.getPlaces()
}