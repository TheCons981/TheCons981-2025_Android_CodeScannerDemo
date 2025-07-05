package it.consoft.codescannerdemo.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import it.consoft.codescannerdemo.ui.components.CodeActionItems
import it.consoft.codescannerdemo.ui.components.CodeDetails
import it.consoft.codescannerdemo.ui.components.CodePersistenceActionButtons
import it.consoft.codescannerdemo.viewModels.CodeScannerViewModel

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun CodeScannerScreen(modifier: Modifier, viewModel: CodeScannerViewModel){

    val context = LocalContext.current
    val scannedCode by viewModel.getScannedCode().collectAsState()
    val errorMessage by viewModel.getMessage().collectAsState()

    val onScanQrCode: () -> (Unit) = {
        viewModel.scanQrCode(context)
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        //When the user has selected a photo, its URL is returned here
        uri?.let {
            viewModel.readQrCode(context, it)
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage.let {
            if (!it.isEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.setMessage("")
            }
        }
    }

    Scaffold(
        modifier = Modifier.padding(0.dp),
        content = { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .wrapContentHeight()
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (scannedCode != null){

                    CodeDetails(
                        textModifier = Modifier.padding().align(Alignment.CenterHorizontally),
                        scannedCode!!
                    )
                    CodePersistenceActionButtons(onConfirm = {
                        viewModel.storeCode(context)
                    }, onCancel = {
                        viewModel.setScannedCode(null)
                    })
                    Spacer(modifier = Modifier.height(16.dp))
                    CodeActionItems(scannedCode?.rawValue ?: "")
                }
                else{
                    Text(
                        text = "No code scanned yet",
                        modifier = Modifier.padding().align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            onScanQrCode()
                        }
                    ) {
                        Text(text = "Scan Qr/Barcode")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            //On button press, launch the photo picker
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    //Here we request only photos. Change this to .ImageAndVideo if
                                    //you want videos too.
                                    //Or use .VideoOnly if you only want videos.
                                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                    ) {
                        Text(text = "Pick from library")
                    }
                }
            }
        }
    )
}