package com.kabo.a24_makany.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDao {

    @Upsert
    suspend fun upsertPlace(place : PlaceEntity)

    @Delete
    suspend fun deletePlace(place: PlaceEntity)

    @Query("select * from places")
    fun getPlaces() : Flow<List<PlaceEntity>>
}