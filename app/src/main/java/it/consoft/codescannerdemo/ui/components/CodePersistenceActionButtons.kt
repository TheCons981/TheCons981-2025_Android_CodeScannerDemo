package it.consoft.codescannerdemo.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CodePersistenceActionButtons(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Button(
        onClick = {
           onConfirm()
        }
    ) {
        Text(text = "Save to repository")
    }
    Spacer(modifier = Modifier.height(8.dp))
    Button(
        onClick = {
            onCancel()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red,
            contentColor = Color.White
        )

    ) {
        Text(text = "Cancel")
    }
}