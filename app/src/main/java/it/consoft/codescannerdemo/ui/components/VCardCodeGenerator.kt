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
fun VCardCodeGenerator(modifier: Modifier, onGenerateCode: (String, BarcodeFormatEnum) -> Unit) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var org by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = name,
        onValueChange = { name = it },
        label = { Text("Name") })
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = surname,
        onValueChange = { surname = it },
        label = { Text("Surname") })
    OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
    OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = email, onValueChange = { email = it }, label = { Text("Email") })
    OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = org, onValueChange = { org = it }, label = { Text("Company") })
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = address,
        onValueChange = { address = it },
        label = { Text("Address") })
    Spacer(Modifier.height(16.dp))
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            val vcard = """
                    BEGIN:VCARD
                    VERSION:3.0
                    N:$surname;$name
                    FN:$name $surname
                    ORG:$org
                    TEL:$phone
                    EMAIL:$email
                    ADR:$address
                    END:VCARD
                """.trimIndent()
            onGenerateCode(vcard, BarcodeFormatEnum.QR_CODE)
        },
        enabled = name.isNotBlank() && surname.isNotBlank()
    ) {
        Text("Generate")
    }
}