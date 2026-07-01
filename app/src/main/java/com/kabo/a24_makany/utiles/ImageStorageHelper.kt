package com.kabo.a24_makany.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

class ImageStorageHelper(
    private val context: Context
) {

    fun saveImage(uri: Uri): String {

        val inputStream = context.contentResolver.openInputStream(uri)

        val imagesDir = File(context.filesDir, "makany")

        if (!imagesDir.exists()) {
            imagesDir.mkdirs()
        }

        val imageFile = File(
            imagesDir,
            "${UUID.randomUUID()}.jpg"
        )

        inputStream?.use { input ->
            imageFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return imageFile.absolutePath
    }
}