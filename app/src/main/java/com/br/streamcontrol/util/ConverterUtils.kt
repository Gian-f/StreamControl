package com.br.streamcontrol.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream


fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
    var uri: Uri? = null
    val bytes = ByteArrayOutputStream()

    try {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val imagePath: String = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            "image",
            null
        )
        uri = Uri.parse(imagePath)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            bytes.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return uri
}


fun uriToBitmap(context: Context, photoUri: Uri) {
    var imageStream: InputStream? = null
    try {
        imageStream = context.contentResolver.openInputStream(photoUri)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    BitmapFactory.decodeStream(imageStream)
}