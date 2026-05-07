package com.kabo.a24_makany.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kabo.a24_makany.R

// Set of Material typography styles to start with

val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.o_bold)),
        fontSize = 22.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.o_semibold)),
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.o_regular)),
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.o_light)),
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.o_regular)),
        fontSize = 11.sp
    )
)