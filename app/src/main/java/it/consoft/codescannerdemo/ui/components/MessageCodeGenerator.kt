package it.consoft.codescannerdemo.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.consoft.codescannerdemo.models.enums.BarcodeFormatEnum

@Composable
fun MessageCodeGenerator(modifier: Modifier, onGenerateCode: (String, BarcodeFormatEnum) -> Unit) {
    var phone by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = phone,
        onValueChange = { phone = it },
        label = { Text("Phone Number") })
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = message,
        onValueChange = { message = it },
        label = { Text("Message") })
    Spacer(Modifier.height(16.dp))
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            val sms = if (message.isNotBlank()) {
                "SMSTO:$phone:$message"
            } else {
                "SMSTO:$phone"
            }
            onGenerateCode(sms, BarcodeFormatEnum.QR_CODE)
        },
        enabled = phone.isNotBlank()
    ) {
        Text("Generate")
    }
}