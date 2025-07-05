package it.consoft.codescannerdemo.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import it.consoft.codescannerdemo.models.enums.BarcodeFormatEnum
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VCalendarCodeGenerator(
    modifier: Modifier = Modifier,
    onGenerateCode: (String, BarcodeFormatEnum) -> Unit
) {
    var summary by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf<Calendar?>(null) }
    var endDate by remember { mutableStateOf<Calendar?>(null) }

    val context = LocalContext.current
    val dateTimeFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.getDefault())

    fun showDateTimePicker(onDateTimeSelected: (Calendar) -> Unit) {
        val now = Calendar.getInstance()
        DatePickerDialog(context, { _, year, month, day ->
            TimePickerDialog(context, { _, hour, minute ->
                val cal = Calendar.getInstance().apply {
                    set(year, month, day, hour, minute, 0)
                }
                onDateTimeSelected(cal)
            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true).show()
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show()
    }


    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = summary,
        onValueChange = { summary = it },
        label = { Text("Event title") })
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = description,
        onValueChange = { description = it },
        label = { Text("Description") })
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = location,
        onValueChange = { location = it },
        label = { Text("Location") })

    Spacer(Modifier.height(8.dp))

    // Start date picker
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDateTimePicker { cal -> startDate = cal }
            },
        value = startDate?.let { dateTimeFormat.format(it.time) } ?: "",
        onValueChange = {},
        label = { Text("Start Time") },
        readOnly = true,
        enabled = true,
        trailingIcon = {
            IconButton(onClick = { showDateTimePicker { cal -> startDate = cal } }) {
                Icon(Icons.Default.Event, contentDescription = "Pick start date")
            }
        }
    )

    Spacer(Modifier.height(8.dp))

    // End date picker
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDateTimePicker { cal -> endDate = cal }
            },
        value = endDate?.let { dateTimeFormat.format(it.time) } ?: "",
        onValueChange = {},
        label = { Text("End Time") },
        readOnly = true,
        enabled = true,
        trailingIcon = {
            IconButton(onClick = { showDateTimePicker { cal -> endDate = cal } }) {
                Icon(Icons.Default.Event, contentDescription = "Pick end date")
            }
        }
    )

    Spacer(Modifier.height(16.dp))

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            val event = """
                    BEGIN:VCALENDAR
                    VERSION:2.0
                    BEGIN:VEVENT
                    SUMMARY:$summary
                    DESCRIPTION:$description
                    LOCATION:$location
                    DTSTART:${startDate?.let { dateTimeFormat.format(it.time) } ?: ""}
                    DTEND:${endDate?.let { dateTimeFormat.format(it.time) } ?: ""}
                    END:VEVENT
                    END:VCALENDAR
                """.trimIndent()
            onGenerateCode(event, BarcodeFormatEnum.QR_CODE)
        },
        enabled = summary.isNotBlank() && startDate != null && endDate != null
    ) {
        Text("Generate")
    }
}