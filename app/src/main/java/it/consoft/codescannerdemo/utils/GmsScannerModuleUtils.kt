package it.consoft.codescannerdemo.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.android.gms.common.api.OptionalModuleApi
import com.google.android.gms.common.moduleinstall.InstallStatusListener
import com.google.android.gms.common.moduleinstall.ModuleAvailabilityResponse
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallClient
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.google.mlkit.vision.common.InputImage
import it.consoft.codescannerdemo.models.ScannedCode
import it.consoft.codescannerdemo.models.enums.BarcodeFormatEnum
import it.consoft.codescannerdemo.models.listeners.ModuleInstallProgressListener
import java.io.InputStream

class GmsScannerModuleUtils() {

    fun checkGmsScannerModule(
        context: Context,
        onModuleInstallComplete: (Boolean, String) -> (Unit)
    ) {
        val moduleInstallClient = ModuleInstall.getClient(context)

        val optionalModuleApi: OptionalModuleApi = GmsBarcodeScanning.getClient(context)
        moduleInstallClient
            .areModulesAvailable(optionalModuleApi)
            .addOnSuccessListener { response: ModuleAvailabilityResponse ->
                if (response.areModulesAvailable()) {
                    //prepare scanner
                    onModuleInstallComplete(true, "")

                } else {
                    // Modules are not present on the device...
                    //barcodeResultView.setText("Modules are not present on the device")
                    moduleInstall(context, moduleInstallClient) { installed, error ->
                        onModuleInstallComplete(installed, error)
                    }
                }
            }
            .addOnFailureListener { e: Exception? ->
                // Handle failure…
                //barcodeResultView.setText("Handle failure…")
                Log.d("moduleinstallclient", "exception: $e ")
                onModuleInstallComplete(false, e?.message ?: "")
            }
    }

    fun prepareScanner(
        context: Context,
        onScanSuccess: (ScannedCode?) -> (Unit)
    ) {

        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_PDF417,
            )
            .enableAutoZoom()
            .build()

        // Modules are present on the device...
        val gmsBarcodeScannerClient = GmsBarcodeScanning.getClient(context, options)

        gmsBarcodeScannerClient.startScan()
            .addOnSuccessListener { barcode ->
                //_barcodeRawValue.value = barcode?.rawValue ?: "";
                barcode?.let {
                    onScanSuccess(ScannedCode(
                        it.rawValue!!,
                        BarcodeFormatEnum.fromGmsBarcodeFormat(it.format),
                        null
                    ))
                } ?: run { onScanSuccess(null) }
            }
            .addOnCanceledListener {
                // Task canceled
                Log.d("scan_canceled_listener", "canceled scan")
            }
            .addOnFailureListener { e ->
                Log.d("scan_failed_exception", "scanQrCode: $e")
            }
    }

    private fun moduleInstall(
        context: Context,
        moduleInstallClient: ModuleInstallClient,
        onModuleInstallResult: (Boolean, String) -> (Unit)
    ) {
        val listener: InstallStatusListener = ModuleInstallProgressListener(moduleInstallClient)
        val optionalModuleApi: OptionalModuleApi = GmsBarcodeScanning.getClient(context)
        val moduleInstallRequest = ModuleInstallRequest.newBuilder()
            .addApi(optionalModuleApi) // Add more API if you would like to request multiple optional modules
            //.addApi(...)
            // Set the listener if you need to monitor the download progress
            .setListener(listener)
            .build()
        moduleInstallClient.installModules(moduleInstallRequest)
            .addOnSuccessListener { response ->
                if (response.areModulesAlreadyInstalled()) {
                    // Modules are already installed when the request is sent.
                    //barcodeResultView.setText("Modules are already installed when the request is sent.")
                    Log.d("moduleinstall", "moduleInstall: module already installed")
                }
                onModuleInstallResult(true, "")
            }
            .addOnFailureListener { e ->
                // Handle failure...
                //barcodeResultView.setText(getErrorMessage(e))
                Log.d("module install failed", "moduleInstall: $e")
                onModuleInstallResult(false, e.message ?: "")
            }
    }

    fun readQrCodeData(
        context: Context,
        uri: Uri,
        onReadResult: (ScannedCode?, String?) -> (Unit)){
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            if (bitmap != null) {
                val image = InputImage.fromBitmap(bitmap, 0)
                // You can specify formats if you want, here we allow all
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_CODE_128,
                        Barcode.FORMAT_EAN_13,
                        Barcode.FORMAT_EAN_8,
                        Barcode.FORMAT_PDF417,
                    )
                    .build()
                val scanner = BarcodeScanning.getClient(options)
                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        if (barcodes.isNotEmpty()) {
                            val barcode = barcodes.first()
                            // Just take the first found
                            onReadResult(
                                ScannedCode(barcode.rawValue!!, BarcodeFormatEnum.fromGmsBarcodeFormat(barcode.format), null),
                                null
                            )
                        } else {
                            onReadResult(
                                null,
                                "No barcode found in the image"
                            )
                        }
                    }
                    .addOnFailureListener { e ->
                        onReadResult(
                            null,
                            "Error while analyzing the image: ${e.message}"
                        )
                    }
            } else {
                onReadResult(
                    null,
                    "unable to read image"
                )
            }
        } catch (e: Exception) {
            onReadResult(
                null,
                "Generic error: ${e.message}"
            )
        }
    }

}