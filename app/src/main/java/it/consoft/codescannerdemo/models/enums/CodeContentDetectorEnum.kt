package it.consoft.codescannerdemo.models.enums

enum class CodeContentDetectorEnum {
    TEXT,
    LINK,
    VCARD,
    EMAIL,
    SMS,
    EVENT,
    PHONE,
    LOCATION,
    BARCODE;

    companion object {
        fun getActionText(type: CodeContentDetectorEnum): String {
            return when (type) {
                TEXT -> ""
                LINK -> "Open Link"
                VCARD -> "Save Contact"
                EMAIL -> "Send Email"
                SMS -> "Send Sms"
                EVENT -> "Save Calendar Event"
                PHONE -> "Call Number"
                LOCATION -> "See Directions"
                BARCODE -> ""
            }
        }
    }

}