package com.marcossan.freshfy.utils

import android.content.Context
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.marcossan.freshfy.viewmodels.ProductViewModel

//class BarcodeScanner(
//    private val context: Context,
//    private val listener: BarcodeListener,
//    private val previewView: PreviewView,
////    private val navController: NavController
//) {
//
//    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
//    private lateinit var barcodeScanner: BarcodeScanner
//
//    init {
//        val options = BarcodeScannerOptions.Builder()
//            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
//            .build()
//
//        barcodeScanner = BarcodeScanning.getClient(options)
//    }
//
//    fun startScan() {//previewView: PreviewView
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
//
//        cameraProviderFuture.addListener({
//            val cameraProvider = cameraProviderFuture.get()
//
//            val preview = Preview.Builder()
//                .build()
//                .also {
//                    it.setSurfaceProvider(previewView.surfaceProvider)
//                }
//
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//            val analysis = ImageAnalysis.Builder()
//                .setTargetResolution(android.util.Size(640, 480))
//                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                .build()
//
//            analysis.setAnalyzer(cameraExecutor, BarcodeAnalyzer(barcodeScanner, listener))
//
//            cameraProvider.bindToLifecycle(
//                context as LifecycleOwner,
//                cameraSelector,
//                preview,
//                analysis
//            )
//        }, ContextCompat.getMainExecutor(context))
//    }
//
//    interface BarcodeListener {
//        fun onBarcodeScanned(barcode: String)
//    }
//
//    private class BarcodeAnalyzer(
//        private val barcodeScanner: BarcodeScanner,
//        private val listener: BarcodeListener
//    ) : ImageAnalysis.Analyzer {
//
//        @OptIn(ExperimentalGetImage::class)
//        override fun analyze(imageProxy: ImageProxy) {
//            val mediaImage = imageProxy.image
//            if (mediaImage != null) {
//                val image = com.google.mlkit.vision.common.InputImage.fromMediaImage(
//                    mediaImage,
//                    imageProxy.imageInfo.rotationDegrees
//                )
//
//                barcodeScanner.process(image)
//                    .addOnSuccessListener { barcodes ->
//                        for (barcode in barcodes) {
//                            val value = barcode.displayValue ?: ""
//                            listener.onBarcodeScanned(value)
//                        }
//                        imageProxy.close()
//                    }
//                    .addOnFailureListener { exception ->
//                        Log.e(
//                            "BarcodeScanner",
//                            "Error al procesar el escaneo: ${exception.message}"
//                        )
//                        imageProxy.close()
//                    }
//            }
//        }
//    }
//}



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