package it.consoft.codescannerdemo.ui.components

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
fun CallCodeGenerator(modifier: Modifier, onGenerateCode: (String, BarcodeFormatEnum) -> Unit) {

    var phone by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = phone,
        onValueChange = { phone = it },
        label = { Text("Phone number") })
    Spacer(Modifier.height(16.dp))
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onGenerateCode("tel:$phone", BarcodeFormatEnum.QR_CODE) },
        enabled = phone.isNotBlank()
    ) {
        Text("Generate")
    }
}