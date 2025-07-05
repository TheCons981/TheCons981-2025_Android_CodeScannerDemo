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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import it.consoft.codescannerdemo.database.entities.CodeEntity
import it.consoft.codescannerdemo.models.ScannedCode
import it.consoft.codescannerdemo.models.enums.BarcodeFormatEnum
import it.consoft.codescannerdemo.ui.components.CodeActionItems
import it.consoft.codescannerdemo.ui.components.CodeDetails
import it.consoft.codescannerdemo.ui.components.CodePersistenceActionButtons
import it.consoft.codescannerdemo.ui.components.CodeImage
import it.consoft.codescannerdemo.utils.ImageUtils
import it.consoft.codescannerdemo.viewModels.CodeRepositoryViewModel
import it.consoft.codescannerdemo.viewModels.CodeScannerViewModel


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun CodeRepositoryDetailScreen(
    onDismissRequest: () -> Unit,
    codeEntity: CodeEntity
){

    val context = LocalContext.current
    val scannedCode = ScannedCode.fromCodeEntity(context, codeEntity)

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),

        ) {

        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .padding(WindowInsets.safeDrawing.asPaddingValues())
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                IconButton(onClick = onDismissRequest) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = Color.Red,
                        modifier = Modifier.size(40.dp)
                    )
                }

                CodeDetails(
                    textModifier = Modifier.padding().align(Alignment.CenterHorizontally),
                    scannedCode
                )
                CodeActionItems(scannedCode.rawValue)
            }
        }
    }
}