package it.consoft.codescannerdemo.models.enums

enum class BarcodeGeneratorTypeEnum(val label: String) {
    CODE_128("Code 128"),
    EAN_8("EAN-8"),
    EAN_13("EAN-13"),
    PDF_417("PDF417")
}