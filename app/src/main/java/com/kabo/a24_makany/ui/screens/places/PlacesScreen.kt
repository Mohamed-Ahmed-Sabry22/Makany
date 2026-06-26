package com.kabo.a24_makany.ui.screens.places

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.kabo.a24_makany.ui.components.MakanySearchBar
import com.kabo.a24_makany.ui.components.PlaceCard
import com.kabo.a24_makany.ui.screens.sheets.PlaceDetailesSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen() {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showPlaceDetailesSheet by rememberSaveable { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MakanySearchBar(
            searchQuery = searchQuery,
            onQueryChanges = { searchQuery = it }
        )
        LazyColumn() {
            items(10) {
                PlaceCard("ElSoban Cafe", "floor four second home", 4.9, {
                    showPlaceDetailesSheet = true
                })
            }
        }
    }
    val context = LocalContext.current
    if (showPlaceDetailesSheet) {
        PlaceDetailesSheet(
            onGoClick = {
                Toast.makeText(context, "gone done", Toast.LENGTH_SHORT).show()
                /*
                لما يدوس علي زرار جو
            */ },
            onShareClick = {
                Toast.makeText(context, "place shared", Toast.LENGTH_SHORT).show()
                /*
                                   لما يدوس علي زرار مشاركة
                             */ },
            onDismiss = {showPlaceDetailesSheet = false}
        )
    }
}

