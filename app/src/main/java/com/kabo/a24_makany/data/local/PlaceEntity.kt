package com.kabo.a24_makany.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name: String,
    val notes: String,
    val category: String,
    val latitude: Double?,
    val longitude: Double?,
    val address: String,
    val imageUri: String?
)