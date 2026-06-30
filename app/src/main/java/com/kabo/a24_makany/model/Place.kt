package com.kabo.a24_makany.model

data class Place(
    val name: String,
    val notes: String,
    val category: String,
    val latitude: Double?,
    val longitude: Double?,
    val address: String,
    val imageUri: String?
)
