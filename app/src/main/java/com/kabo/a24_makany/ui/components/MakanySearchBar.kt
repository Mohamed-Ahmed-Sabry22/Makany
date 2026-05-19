package com.kabo.a24_makany.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kabo.a24_makany.ui.theme.Primary
import com.kabo.a24_makany.ui.theme.Surface

@Composable
fun MakanySearchBar(searchQuery: String , onQueryChanges : (String) -> Unit ) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onQueryChanges,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("Search saved places...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null
            )
        },
        shape = RoundedCornerShape(50.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Surface,
            focusedContainerColor = Surface,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Primary
        ),
        singleLine = true
    )
}
