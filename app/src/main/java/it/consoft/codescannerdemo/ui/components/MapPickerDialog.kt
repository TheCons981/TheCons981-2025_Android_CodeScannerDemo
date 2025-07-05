package it.consoft.codescannerdemo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.*

@Composable
fun MapPickerDialog(
    onDismiss: () -> Unit,
    onLocationSelected: (Double, Double) -> Unit,
    initialLat: Float,
    initialLon: Float
) {
    var selectedPosition by remember { mutableStateOf(LatLng(initialLat.toDouble(), initialLon.toDouble())) }

    val markerState = remember { MarkerState(position = selectedPosition) }
    LaunchedEffect(selectedPosition) {
        markerState.position = selectedPosition
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onLocationSelected(selectedPosition.latitude, selectedPosition.longitude)
            }) {
                Text("Select")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = null,
        text = {
            Box(Modifier.size(300.dp)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(selectedPosition, 12f)
                    },
                    onMapClick = { latLng ->
                        selectedPosition = latLng
                    }
                ) {
                    Marker(
                        state = markerState,
                        title = "Location selected"
                    )
                }
            }
        }
    )
}