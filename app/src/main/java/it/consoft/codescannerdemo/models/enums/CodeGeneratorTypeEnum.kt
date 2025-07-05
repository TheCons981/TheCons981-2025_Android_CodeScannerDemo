package it.consoft.codescannerdemo.models.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.ui.graphics.vector.ImageVector

enum class CodeGeneratorTypeEnum(val label: String, val icon: ImageVector) {
    TEXT("Text/Link", Icons.Default.TextFields),
    VCARD("Contact", Icons.Default.Person),
    EMAIL("Email", Icons.Default.Email),
    SMS("Message", Icons.Default.Message),
    EVENT("Calendar Event", Icons.Default.Event),
    PHONE("Call", Icons.Default.Phone),
    LOCATION("Position", Icons.Default.LocationOn),
    BARCODE("Barcode", Icons.Default.QrCode)
}