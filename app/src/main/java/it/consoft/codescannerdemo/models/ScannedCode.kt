package it.consoft.codescannerdemo.models

import android.content.Context
import android.graphics.Bitmap
import it.consoft.codescannerdemo.database.entities.CodeEntity
import it.consoft.codescannerdemo.models.enums.BarcodeFormatEnum
import it.consoft.codescannerdemo.utils.ImageUtils
import kotlinx.serialization.Serializable

data class ScannedCode(
    var rawValue: String = "",
    var format: BarcodeFormatEnum = BarcodeFormatEnum.UNKNOWN,
    var image: Bitmap?
) {
    companion object {
        fun fromCodeEntity (
            context: Context,
            codeEntity: CodeEntity
        ): ScannedCode {
            var image: Bitmap? = null
            codeEntity.imageUrl?.let {
                image = ImageUtils.getBitmapFromFiles(context, codeEntity.imageUrl)
            }

            return ScannedCode(
                codeEntity.code,
                BarcodeFormatEnum.fromCode(codeEntity.format) ?: BarcodeFormatEnum.UNKNOWN,
                image
            )

        }
    }
}