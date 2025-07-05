package it.consoft.codescannerdemo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.consoft.codescannerdemo.models.enums.BarcodeFormatEnum
import it.consoft.codescannerdemo.models.enums.BarcodeGeneratorTypeEnum

@Composable
fun BarcodeCodeGenerator(modifier: Modifier, onGenerateCode: (String, BarcodeFormatEnum) -> Unit) {
    var selectedType by remember { mutableStateOf(BarcodeGeneratorTypeEnum.CODE_128) }
    var value by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        BarcodeGeneratorTypeEnum.entries.forEach { type ->
            FilterChip(
                selected = selectedType == type,
                onClick = { selectedType = type },
                label = { Text(type.label) },
                modifier = Modifier.padding(3.dp),
            )
        }
    }

    Spacer(Modifier.height(24.dp))

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = {
            value = it
            errorText = null
        },
        label = { Text("Value") },
        isError = errorText != null,
        singleLine = true,
    )
    if (errorText != null) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = errorText!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }

    Spacer(Modifier.height(24.dp))

    Button(
        modifier = Modifier.fillMaxWidth(),
        enabled = value.isNotBlank(),
        onClick = {
            val valid = when (selectedType) {
                BarcodeGeneratorTypeEnum.CODE_128 -> value.isNotEmpty()
                BarcodeGeneratorTypeEnum.EAN_8 -> value.matches(Regex("^\\d{8}$"))
                BarcodeGeneratorTypeEnum.EAN_13 -> value.matches(Regex("^\\d{13}$"))
                BarcodeGeneratorTypeEnum.PDF_417 -> value.isNotEmpty()
            }
            if (!valid) {
                errorText = when (selectedType) {
                    BarcodeGeneratorTypeEnum.EAN_8 -> "EAN-8 must contains 8 numeric characters"
                    BarcodeGeneratorTypeEnum.EAN_13 -> "EAN-13 must contains 13 numeric characters"
                    else -> "Not valid value"
                }
            } else {
                errorText = null
                onGenerateCode(value, BarcodeFormatEnum.fromBarcodeGeneratorTypeEnum(selectedType))
            }
        }
    ) {
        Text("Generate")
    }
}