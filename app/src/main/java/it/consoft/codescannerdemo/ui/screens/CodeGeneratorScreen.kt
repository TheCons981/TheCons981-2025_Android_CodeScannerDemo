package it.consoft.codescannerdemo.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import it.consoft.codescannerdemo.models.enums.BarcodeFormatEnum
import it.consoft.codescannerdemo.models.enums.CodeGeneratorTypeEnum
import it.consoft.codescannerdemo.ui.components.BarcodeCodeGenerator
import it.consoft.codescannerdemo.ui.components.CallCodeGenerator
import it.consoft.codescannerdemo.ui.components.CodePersistenceActionButtons
import it.consoft.codescannerdemo.ui.components.CodeImage
import it.consoft.codescannerdemo.ui.components.EmailCodeGenerator
import it.consoft.codescannerdemo.ui.components.LocationCodeGenerator
import it.consoft.codescannerdemo.ui.components.MessageCodeGenerator
import it.consoft.codescannerdemo.ui.components.TextCodeGenerator
import it.consoft.codescannerdemo.ui.components.VCalendarCodeGenerator
import it.consoft.codescannerdemo.ui.components.VCardCodeGenerator
import it.consoft.codescannerdemo.viewModels.CodeGeneratorViewModel

@Composable
fun CodeGeneratorScreen(
    modifier: Modifier,
    viewModel: CodeGeneratorViewModel
) {
    val context = LocalContext.current
    val type by viewModel.getSelectedType().collectAsState()
    val generatedCode by viewModel.getGeneratedCode().collectAsState()
    val message by viewModel.getMessage().collectAsState()

    val onGenerateCode: (String, BarcodeFormatEnum) -> (Unit) = { rawValue, format ->
        viewModel.generateCode(rawValue, format)
    }

    LaunchedEffect(message) {
        message.let {
            if (!it.isEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.setMessage("")
            }
        }
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .wrapContentHeight()
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (type) {
            CodeGeneratorTypeEnum.TEXT -> TextCodeGenerator(
                modifier,
                onGenerateCode = onGenerateCode
            )

            CodeGeneratorTypeEnum.VCARD -> VCardCodeGenerator(
                modifier,
                onGenerateCode = onGenerateCode
            )

            CodeGeneratorTypeEnum.EMAIL -> EmailCodeGenerator(
                modifier,
                onGenerateCode = onGenerateCode
            )

            CodeGeneratorTypeEnum.SMS -> MessageCodeGenerator(
                modifier,
                onGenerateCode = onGenerateCode
            )

            CodeGeneratorTypeEnum.EVENT -> VCalendarCodeGenerator(
                modifier,
                onGenerateCode = onGenerateCode
            )

            CodeGeneratorTypeEnum.PHONE -> CallCodeGenerator(
                modifier,
                onGenerateCode = onGenerateCode
            )

            CodeGeneratorTypeEnum.LOCATION -> LocationCodeGenerator(
                modifier,
                onGenerateCode = onGenerateCode
            )

            CodeGeneratorTypeEnum.BARCODE -> BarcodeCodeGenerator(
                modifier,
                onGenerateCode = onGenerateCode
            )
        }

        generatedCode?.image?.let {
            Spacer(Modifier.height(26.dp))
            CodeImage(modifier, it)
            Spacer(modifier = Modifier.height(16.dp))
            CodePersistenceActionButtons(onConfirm = {
                viewModel.storeCode(context)
            }, onCancel = {
                viewModel.setGeneratedCode(null)
            })
        }
    }

}