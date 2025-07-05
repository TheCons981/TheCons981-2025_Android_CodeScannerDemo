package it.consoft.codescannerdemo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import kotlin.uuid.Uuid

object ImageUtils {

    fun saveBitmapToFilesDir(
        context: Context,
        bitmap: Bitmap,
        filename: String = "${UUID.randomUUID()}.png"
    ): Boolean {
        val file = File(context.filesDir, filename)
        return try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun getFromFilesDir(context: Context, filename: String): File {
        return File(context.filesDir, filename)
    }

    fun getBitmapFromFiles(context: Context, filename: String): Bitmap? {
        val file = getFromFilesDir(context, filename)
        return BitmapFactory.decodeFile(file.absolutePath)
    }
}