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
fun TextCodeGenerator(modifier: Modifier, onGenerateCode: (String, BarcodeFormatEnum) -> Unit) {

    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = { text = it },
        label = { Text("Text/Link") }
    )
    Spacer(Modifier.height(16.dp))
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onGenerateCode(text, BarcodeFormatEnum.QR_CODE) },
        enabled = text.isNotBlank()
    ) {
        Text("Generate")
    }
}