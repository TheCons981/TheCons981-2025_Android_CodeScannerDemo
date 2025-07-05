package it.consoft.codescannerdemo.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import androidx.core.content.FileProvider
import it.consoft.codescannerdemo.models.enums.CodeContentDetectorEnum
import androidx.core.net.toUri
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object CodeRawValueDetector {

    val mapUrlSchemes = listOf<String>("maps.app.goo.gl", "comgooglemaps:", "maps.google.com", "maps.apple.com", "geo:", "waze:", "baidumap:")

    fun recognize(text: String): CodeContentDetectorEnum {
        val trimmed = text.trim()

        val urlRegex = Regex("^(https?|ftp)://[\\w\\-?=%.]+\\.[a-z]{2,}.*", RegexOption.IGNORE_CASE)
        if (urlRegex.matches(trimmed)) {
            return CodeContentDetectorEnum.LINK
        }

        if (trimmed.startsWith("mailto:", ignoreCase = true)) {
            val email = trimmed.removePrefix("mailto:").substringBefore("?")
            return CodeContentDetectorEnum.EMAIL
        }
        val emailRegex = Regex("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", RegexOption.IGNORE_CASE)
        if (emailRegex.matches(trimmed)) {
            return CodeContentDetectorEnum.EMAIL
        }

        if (trimmed.startsWith("BEGIN:VCARD", ignoreCase = true)) {
            return CodeContentDetectorEnum.VCARD
        }

        if (trimmed.startsWith("SMSTO:", ignoreCase = true)) {
            return CodeContentDetectorEnum.SMS
        }

        if (trimmed.startsWith("BEGIN:VCALENDAR", ignoreCase = true)) {
            return CodeContentDetectorEnum.EVENT
        }

        if (trimmed.startsWith("TEL:", ignoreCase = true)) {
            return CodeContentDetectorEnum.PHONE
        }

        val lowerInput = trimmed.lowercase()
        if (mapUrlSchemes.any { scheme -> lowerInput.contains(scheme.lowercase()) }) {
            return CodeContentDetectorEnum.LOCATION
        }

        val wwwRegex = Regex("^www\\.[\\w\\-?=%.]+\\.[a-z]{2,}.*", RegexOption.IGNORE_CASE)
        if (wwwRegex.matches(trimmed)) {
            return CodeContentDetectorEnum.LINK
        }

        return CodeContentDetectorEnum.TEXT
    }

    fun getIntentForType(context: Context, rawValue: String): Intent? {
        val type = recognize(rawValue);
        return getIntentForType(context, type, rawValue)
    }

    fun getIntentForType(context: Context, type: CodeContentDetectorEnum, rawValue: String): Intent? {
        return when (type) {
            CodeContentDetectorEnum.LINK -> {
                Intent(Intent.ACTION_VIEW, rawValue.toUri())
            }
            CodeContentDetectorEnum.EMAIL -> Intent(Intent.ACTION_SENDTO).apply {
                data = rawValue.toUri()
            }

            CodeContentDetectorEnum.VCARD -> {
                val fileName = "contact.vcf"
                val file = runCatching {
                    val output = context.openFileOutput(fileName, Context.MODE_PRIVATE)
                    output.write(rawValue.toByteArray())
                    output.close()
                    context.getFileStreamPath(fileName)
                }.getOrNull()
                file?.let {
                    // Usa FileProvider per ottenere il content URI
                    val contentUri: Uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        it
                    )
                    return Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(contentUri, "text/x-vcard")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                }
            }

            CodeContentDetectorEnum.SMS -> Intent(Intent.ACTION_SENDTO).apply {
                val parts = rawValue.trim().removePrefix("SMSTO:").split(":", limit = 2)
                val number = parts.getOrNull(0) ?: ""
                val message = parts.getOrNull(1)
                data = rawValue.toUri()
                putExtra("sms_body", message ?: "")
            }

            CodeContentDetectorEnum.EVENT -> Intent(Intent.ACTION_INSERT).apply {
                val title = CodeRawValueDetector.extractCalendarEventField(rawValue, "SUMMARY")
                val description =
                    CodeRawValueDetector.extractCalendarEventField(rawValue, "DESCRIPTION")
                val location = CodeRawValueDetector.extractCalendarEventField(rawValue, "LOCATION")
                val dtStart = CodeRawValueDetector.parseICalDate(
                    extractCalendarEventField(
                        rawValue,
                        "DTSTART"
                    )
                )
                val dtEnd = parseICalDate(extractCalendarEventField(rawValue, "DTEND"))

                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, title)
                putExtra(CalendarContract.Events.DESCRIPTION, description)
                putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                dtStart?.let { putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, it) }
                dtEnd?.let { putExtra(CalendarContract.EXTRA_EVENT_END_TIME, it) }
            }

            CodeContentDetectorEnum.PHONE -> Intent(Intent.ACTION_DIAL, rawValue.toUri())
            CodeContentDetectorEnum.LOCATION -> Intent(
                Intent.ACTION_VIEW,
                rawValue.toUri()
            )

            CodeContentDetectorEnum.TEXT -> null
            CodeContentDetectorEnum.BARCODE -> null
        }
    }

    private fun extractCalendarEventField(vcal: String, key: String): String? =
        Regex("$key:(.+)").find(vcal)?.groups?.get(1)?.value


    private fun parseICalDate(dateString: String?): Long? {
        return try {
            val df = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.getDefault())
            df.timeZone = TimeZone.getTimeZone("UTC")
            dateString?.let { df.parse(it)?.time }
        } catch (e: Exception) {
            null
        }
    }
}