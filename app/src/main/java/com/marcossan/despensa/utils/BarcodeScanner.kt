package com.marcossan.despensa.utils

import android.content.Context
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.marcossan.despensa.viewmodels.ProductViewModel

class BarcodeScanner(
    appContext: Context,
    private val navController: NavController
) {

    /**
     * From the docs: If you know which barcode formats you expect to read, you can improve the
     * speed of the barcode detector by configuring it to only detect those formats.
     */
    private val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
//        .enableAutoZoom()
//        .allowManualInput() // available on 16.1.0 and higher
        .build()

    private val scanner = GmsBarcodeScanning.getClient(appContext, options)
//    private val barCodeResults = MutableStateFlow<String?>(null)

    suspend fun startScan(viewModel: ProductViewModel) {
        try {
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    // Task completed successfully
//                    barCodeResults.value = barcode.displayValue
                    viewModel.onScannedBarcode(barcode = barcode.displayValue.toString())
                    navController.navigate(route = "add/${barcode.displayValue.toString()}")
//                    navController.navigate(route = Screens.ScannerScreen.route)
                }
                .addOnCanceledListener {
                    // Task cancelled
//                    barCodeResults.value = "canceled"
                    viewModel.onScannedBarcode(barcode = "canceled")
                }
                .addOnFailureListener {
                    // Task failed with an exception
//                    barCodeResults.value = "failed"
                    viewModel.onScannedBarcode(barcode = "failed")
                }
        } catch (e: Exception) {
            println(e)
        }

    }
}