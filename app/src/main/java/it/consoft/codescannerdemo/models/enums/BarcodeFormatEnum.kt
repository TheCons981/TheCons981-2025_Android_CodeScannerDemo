package it.consoft.codescannerdemo.models.enums

import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.zxing.BarcodeFormat


enum class BarcodeFormatEnum(val code: String, val description: String) {

    UNKNOWN("unknown", "Unknown"),
    QR_CODE("qr", "QR Code"),
    CODE_128("code128", "Code 128"),
    EAN_13("ean13", "EAN-13"),
    PDF417("pdf417", "PDF417"),
    EAN_8("ean8", "EAN-8");

    companion object {

        fun fromCode(code: String): BarcodeFormatEnum? =
            entries.find { it.code == code } ?: null

        fun fromGmsBarcodeFormat(format: Int): BarcodeFormatEnum {
            return when (format) {
                Barcode.FORMAT_QR_CODE -> QR_CODE
                Barcode.FORMAT_CODE_128 -> CODE_128
                Barcode.FORMAT_EAN_13 -> EAN_13
                Barcode.FORMAT_EAN_8 -> EAN_8
                Barcode.FORMAT_PDF417 -> PDF417

                else -> {
                    UNKNOWN
                }
            }
        }

        fun toZxingBarcodeFormat(format: BarcodeFormatEnum): BarcodeFormat {
            return when (format) {
                QR_CODE -> BarcodeFormat.QR_CODE
                CODE_128 -> BarcodeFormat.CODE_128
                EAN_13 -> BarcodeFormat.EAN_13
                EAN_8 -> BarcodeFormat.EAN_8
                PDF417 -> BarcodeFormat.PDF_417

                else -> {
                    BarcodeFormat.QR_CODE
                }
            }
        }

        fun fromBarcodeGeneratorTypeEnum(format: BarcodeGeneratorTypeEnum): BarcodeFormatEnum {
            return when (format) {
                BarcodeGeneratorTypeEnum.EAN_8 -> EAN_8
                BarcodeGeneratorTypeEnum.EAN_13 -> EAN_13
                BarcodeGeneratorTypeEnum.CODE_128 -> CODE_128
                BarcodeGeneratorTypeEnum.PDF_417 -> PDF417
            }
        }
    }
}