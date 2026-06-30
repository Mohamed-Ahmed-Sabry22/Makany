package com.kabo.a24_makany.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kabo.a24_makany.ui.theme.Shape

@Composable
fun SheetTextField(
    value : String,
    isError : Boolean = false,
    onValueChange : (String)->Unit,
    lines : Int = 1,
    labelText : String,
    placeHolderText : String
    ) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        minLines = lines,
        label = {
            Text(
                labelText,
                style = MaterialTheme.typography.labelSmall
            )
        },
        placeholder = {
            Text(
                placeHolderText,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF7C7C7C)
            )
        },
        supportingText = {
            if (isError) {
                Text(text = "This field cannot be empty", color = MaterialTheme.colorScheme.error)
            }
        },
        shape = Shape.small
    )
}