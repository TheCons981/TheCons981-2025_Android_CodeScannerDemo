package it.consoft.codescannerdemo.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import it.consoft.codescannerdemo.models.enums.CodeContentDetectorEnum
import it.consoft.codescannerdemo.utils.AppUtils
import it.consoft.codescannerdemo.utils.AppUtils.startActivityWithChooser
import it.consoft.codescannerdemo.utils.CodeRawValueDetector

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun CodeActionItems(codeRawValue: String) {

    val context = LocalContext.current
    val detectedType by remember { mutableStateOf(CodeRawValueDetector.recognize(codeRawValue)) }

    val onGoogleClick: () -> (Unit) = {
        AppUtils.searchWithGoogle(context, codeRawValue)
    }

    val onDuckDuckGoClick: () -> (Unit) = {
        AppUtils.searchWithDuckDuckGo(context, codeRawValue)
    }

    val onBingClick: () -> (Unit) = {
        AppUtils.searchWithBing(context, codeRawValue)
    }

    val onCopyClick: () -> (Unit) = {
        AppUtils.copyToClipboard(context, codeRawValue)
    }

    val onPerformActionClick: () -> (Unit) = {
        val intent = CodeRawValueDetector.getIntentForType(context, detectedType, codeRawValue)
        if (intent != null) {
            startActivityWithChooser(context, intent)
        }
    }

    val options = mutableListOf(
        "Copy To Clipboard" to onCopyClick,
        "Search With Google" to onGoogleClick,
        "Search With DuckDuckGo" to onDuckDuckGoClick,
        "Search With Bing" to onBingClick,
    )

    if (detectedType != CodeContentDetectorEnum.TEXT && detectedType != CodeContentDetectorEnum.BARCODE){
        val text = CodeContentDetectorEnum.getActionText(detectedType)
        options.addFirst(text to onPerformActionClick)
    }

    options.forEachIndexed { index, (label, action) ->
        ListItem(
            modifier = Modifier
                .clickable { action() }
                .fillMaxWidth(),
            headlineContent = { Text(label) }
        )
        if (index < options.size - 1) {
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        }
    }
    /*LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(options.size) { index ->
            val (label, action) = options[index]
            ListItem(
                modifier = Modifier
                    .clickable { action() }
                    .fillMaxWidth(),
                headlineContent = { Text(label) }
            )
            if (index < options.size - 1) {
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }
        }
    }*/
}