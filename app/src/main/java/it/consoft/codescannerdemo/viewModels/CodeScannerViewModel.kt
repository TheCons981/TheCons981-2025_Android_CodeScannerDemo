package it.consoft.codescannerdemo.viewModels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.consoft.codescannerdemo.database.dao.CodeDao
import it.consoft.codescannerdemo.database.entities.CodeEntity
import it.consoft.codescannerdemo.models.ScannedCode
import it.consoft.codescannerdemo.models.enums.BarcodeFormatEnum
import it.consoft.codescannerdemo.utils.BarcodeUtils
import it.consoft.codescannerdemo.utils.GmsScannerModuleUtils
import it.consoft.codescannerdemo.utils.ImageUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CodeScannerViewModel(private val codeDao: CodeDao) : ViewModel() {
    private var _isLoading = MutableStateFlow(false)

    fun setLoading(value: Boolean) {
        _isLoading.value = value
    }

    private var _scannedCode = MutableStateFlow<ScannedCode?>(null)

    fun getScannedCode(): StateFlow<ScannedCode?> {
        return _scannedCode.asStateFlow()
    }

    fun setScannedCode(scannedCode: ScannedCode?) {
        _scannedCode.value = scannedCode
    }


    private var _errorMessage = MutableStateFlow("")
    fun setMessage(message: String) {
        _errorMessage.value = message
    }

    fun getMessage(): StateFlow<String> {
        return _errorMessage.asStateFlow()
    }

    fun scanQrCode(context: Context) {
        val gmsScannerModuleUtils = GmsScannerModuleUtils()
        setLoading(true)
        setMessage("")

        gmsScannerModuleUtils.checkGmsScannerModule(context) { available, reason ->
            if (available) {
                setLoading(false)
                gmsScannerModuleUtils.prepareScanner(context = context) { barcodeData ->
                    //Toast.makeText(context, "Code Scanned: $scannedCode", Toast.LENGTH_LONG).show()
                    barcodeData?.let {
                        if (it.format.equals(BarcodeFormatEnum.QR_CODE)) {
                            it.image = BarcodeUtils.generateQrCode(it.rawValue)
                        } else {
                            it.image = BarcodeUtils.generateBarcode(
                                it.rawValue,
                                BarcodeFormatEnum.toZxingBarcodeFormat(it.format)
                            )
                        }
                    }
                    setScannedCode(barcodeData)
                }
            } else {
                setLoading(false)
                if (reason.isEmpty()) {
                    setMessage(context.resources.getString(it.consoft.codescannerdemo.R.string.gms_scanner_module_error))
                } else {
                    setMessage(reason)
                }
            }
        }
    }

    fun readQrCode(context: Context, uri: Uri) {
        // Read and decode the image
        setMessage("")
        val gmsScannerModuleUtils = GmsScannerModuleUtils()
        gmsScannerModuleUtils.readQrCodeData(context, uri) { barcodeData, error ->
            error?.let {
                if (!it.isEmpty()) setMessage(it)
            }

            barcodeData?.let {
                if (it.format.equals(BarcodeFormatEnum.QR_CODE)) {
                    it.image = BarcodeUtils.generateQrCode(it.rawValue)
                } else {
                    it.image = BarcodeUtils.generateBarcode(
                        it.rawValue,
                        BarcodeFormatEnum.toZxingBarcodeFormat(it.format)
                    )
                }
            }
            setScannedCode(barcodeData)
        }
    }

    fun storeCode(context: Context) {
        _scannedCode.value?.let { code ->
            val imageUrl = "${UUID.randomUUID()}.png"
            val codeEntity =
                CodeEntity(code = code.rawValue, format = code.format.code, imageUrl = imageUrl)
            code.image?.let { image ->
                ImageUtils.saveBitmapToFilesDir(context, image, imageUrl)
            }
            viewModelScope.launch {
                codeDao.insert(codeEntity)
                setMessage("Code saved")
                setScannedCode(null)
            }
        }
    }
}