package com.kabo.a24_makany.utils

import android.graphics.Bitmap
import android.graphics.Path
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun bitmapDescriptorFromColor(color: Color): BitmapDescriptor {
    val colorInt = color.toArgb()

    val paint = android.graphics.Paint().apply {
        this.color = colorInt
        isAntiAlias = true
    }

    val bitmap = Bitmap.createBitmap(90, 120, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)

// الدائرة أكبر
    canvas.drawCircle(30f, 30f, 30f, paint)

// الذيل أكبر
    val path = Path().apply {
        moveTo(18f, 52f)
        lineTo(42f, 52f)
        lineTo(30f, 82f)
        close()
    }
    canvas.drawPath(path, paint)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}