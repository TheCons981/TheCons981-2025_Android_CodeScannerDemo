package it.consoft.codescannerdemo.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.consoft.codescannerdemo.models.enums.BarcodeFormatEnum

@Composable
fun EmailCodeGenerator(modifier: Modifier, onGenerateCode: (String, BarcodeFormatEnum) -> Unit) {
    var email by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        onValueChange = { email = it },
        label = { Text("To") }
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = subject,
        onValueChange = { subject = it },
        label = { Text("Subject") })
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = body, onValueChange = { body = it },
        label = { Text("Body") })
    Spacer(Modifier.height(16.dp))
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            val mailto = buildString {
                append("mailto:$email")
                val params = listOf(
                    "subject" to subject,
                    "body" to body
                ).filter { it.second.isNotBlank() }
                if (params.isNotEmpty()) {
                    append("?")
                    append(params.joinToString("&") { "${it.first}=${Uri.encode(it.second)}" })
                }
            }
            onGenerateCode(mailto, BarcodeFormatEnum.QR_CODE)
        },
        enabled = email.isNotBlank()
    ) {
        Text("Generate")
    }
}