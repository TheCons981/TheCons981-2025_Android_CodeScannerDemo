package it.consoft.codescannerdemo.ui.components

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.*
import it.consoft.codescannerdemo.models.enums.BarcodeFormatEnum

@Composable
fun LocationCodeGenerator(
    modifier: Modifier, onGenerateCode: (String, BarcodeFormatEnum) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var showMapDialog by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf<String?>(null) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            getCurrentLocation(context, fusedLocationClient) { loc, error ->
                if (loc != null) {
                    latitude = loc.latitude.toString()
                    longitude = loc.longitude.toString()
                    locationError = null
                } else {
                    locationError = error ?: "Undefined error"
                }
            }
        } else {
            locationError = "Location permission denied."
        }
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = latitude,
        onValueChange = { latitude = it },
        label = { Text("Latitude") }
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = longitude,
        onValueChange = { longitude = it },
        label = { Text("Longitude") }
    )
    if (locationError != null) {
        Text(locationError!!,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.error)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp, Alignment.CenterHorizontally)
    ) {
        Button(
            onClick = {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }) {
            Text("Get your location")
        }

        Button(onClick = {
            showMapDialog = true
        }) {
            Text("Pick on map")
        }
    }
    Spacer(Modifier.height(16.dp))
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onGenerateCode("geo:$latitude,$longitude", BarcodeFormatEnum.QR_CODE) },
        enabled = latitude.isNotBlank() && longitude.isNotBlank()
    ) {
        Text("Generate")
    }

    if (showMapDialog) {
        MapPickerDialog(
            onDismiss = { showMapDialog = false },
            onLocationSelected = { lat, lon ->
                latitude = lat.toString()
                longitude = lon.toString()
                showMapDialog = false
            },
            initialLat = latitude.toFloatOrNull() ?: 41.9028f,
            initialLon = longitude.toFloatOrNull() ?: 12.4964f
        )
    }
}

fun getCurrentLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationResult: (Location?, String?) -> Unit
) {
    try {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setMaxUpdates(1)
            .build()
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    fusedLocationClient.removeLocationUpdates(this)
                    val location = result.lastLocation
                    if (location != null) {
                        onLocationResult(location, null)
                    } else {
                        onLocationResult(null, "Unable to get your position")
                    }
                }
            },
            Looper.getMainLooper()
        )
    } catch (e: SecurityException) {
        onLocationResult(null, "Permission not provided.")
    }
}