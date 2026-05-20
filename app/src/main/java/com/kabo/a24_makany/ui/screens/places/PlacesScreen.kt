package com.kabo.a24_makany.ui.screens.places

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Directions
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShareLocation
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kabo.a24_makany.R
import com.kabo.a24_makany.ui.components.MakanySearchBar
import com.kabo.a24_makany.ui.components.PlaceCard
import com.kabo.a24_makany.ui.screens.sheets.PlaceDetailesSheet
import com.kabo.a24_makany.ui.theme.Primary
import com.kabo.a24_makany.ui.theme.Secondary
import com.kabo.a24_makany.ui.theme.Shape
import com.kabo.a24_makany.ui.theme.Surface

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
    if (showPlaceDetailesSheet) {
        PlaceDetailesSheet(
            onGoClick = { /* هنا بعدين */ },
            onShareClick = { /* هنا بعدين */ },
            onDismiss = {showPlaceDetailesSheet = false}
        )
    }
}

