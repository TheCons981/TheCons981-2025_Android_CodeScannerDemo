package it.consoft.codescannerdemo.viewModels

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.consoft.codescannerdemo.database.dao.CodeDao
import it.consoft.codescannerdemo.database.entities.CodeEntity
import it.consoft.codescannerdemo.models.ScannedCode
import it.consoft.codescannerdemo.models.enums.BarcodeFormatEnum
import it.consoft.codescannerdemo.models.enums.CodeGeneratorTypeEnum
import it.consoft.codescannerdemo.utils.BarcodeUtils
import it.consoft.codescannerdemo.utils.ImageUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID


class CodeGeneratorViewModel(private val codeDao: CodeDao) : ViewModel() {

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var _errorMessage = MutableStateFlow("")

    fun getMessage(): StateFlow<String> {
        return _errorMessage.asStateFlow()
    }

    fun setMessage(message: String) {
        _errorMessage.value = message
    }

    private var _generatedCode = MutableStateFlow<ScannedCode?>(null)

    fun getGeneratedCode(): StateFlow<ScannedCode?> {
        return _generatedCode.asStateFlow()
    }

    fun setGeneratedCode(generatedCode: ScannedCode?) {
        _generatedCode.value = generatedCode
    }

    private var _selectedType = MutableStateFlow<CodeGeneratorTypeEnum>(CodeGeneratorTypeEnum.TEXT)

    fun getSelectedType(): StateFlow<CodeGeneratorTypeEnum> {
        return _selectedType.asStateFlow()
    }

    fun setSelectedType(selectedType: CodeGeneratorTypeEnum) {
        _selectedType.value = selectedType
    }

    fun generateCode(rawValue: String, format: BarcodeFormatEnum) {
        _generatedCode.value = null
        val image: Bitmap = if (format == BarcodeFormatEnum.QR_CODE) {
            BarcodeUtils.generateQrCode(rawValue)
        } else {
            BarcodeUtils.generateBarcode(
                rawValue,
                BarcodeFormatEnum.toZxingBarcodeFormat(format)
            )
        }
        _generatedCode.value = ScannedCode(rawValue, format, image);
    }

    fun storeCode(context: Context) {
        _generatedCode.value?.let { code ->
            val imageUrl = "${UUID.randomUUID()}.png"
            val codeEntity =
                CodeEntity(code = code.rawValue, format = code.format.code, imageUrl = imageUrl)
            code.image?.let { image ->
                ImageUtils.saveBitmapToFilesDir(context, image, imageUrl)
            }
            viewModelScope.launch {
                codeDao.insert(codeEntity)
                setMessage("Code saved")
                setGeneratedCode(null)
            }
        }
    }
}