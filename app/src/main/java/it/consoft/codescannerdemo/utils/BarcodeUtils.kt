package it.consoft.codescannerdemo.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

object BarcodeUtils {

    private const val QR_SIZE = 600
    private const val BARCODE_WIDTH = 600
    private const val BARCODE_HEIGHT = 250

    fun generateQrCode(content: String): Bitmap {
        return generateBitmap(content, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE)
    }

    fun generateBarcode(content: String, format: BarcodeFormat): Bitmap {
        return generateBitmap(content, format, BARCODE_WIDTH, BARCODE_HEIGHT)
    }

    private fun generateBitmap(content: String, format: BarcodeFormat, width: Int, height: Int): Bitmap {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, format, width, height)
        val bitmap = createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap[x, y] =
                    if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
            }
        }
        return bitmap
    }
}