package com.marcossan.freshfy.viewmodels

//class BarcodeViewModel(application: Application) : AndroidViewModel(application),
//    BarcodeScanner.BarcodeListener {
//
//    private val _barcode = MutableLiveData<String>()
//    val barcode: LiveData<String> get() = _barcode
//
//    val previewView = PreviewView(application)
//    // Lógica de inicialización del escáner de códigos de barras
////    private val barcodeScanner = BarcodeScanner(application.applicationContext, this)
//    private val barcodeScanner = BarcodeScanner(application, object : BarcodeScanner.BarcodeListener {
//        override fun onBarcodeScanned(barcode: String) {
//            // Lógica para manejar el código de barras escaneado en tu ComposeActivity
//        }
//    }, previewView)
//    init {
//        val previewView = PreviewView(application) // Puedes crear y configurar el PreviewView según tus necesidades
////        barcodeScanner.startScan(previewView)
//        // Iniciar el escáner cuando se crea el ViewModel
////        barcodeScanner.startScan(previewView)
//        barcodeScanner.startScan()
//    }
//
//    override fun onBarcodeScanned(barcode: String) {
//        // Notificar a la interfaz de usuario cuando se escanea un código de barras
//        _barcode.postValue(barcode)
//    }
//
//    // Llamar a esta función desde la interfaz de usuario si es necesario detener el escáner
//    fun stopScan() {
//        // Implementa la lógica para detener el escáner si es necesario
//    }
//
//    // Asegúrate de liberar recursos cuando el ViewModel es desechado
//    override fun onCleared() {
//        // Implementa la lógica para liberar recursos, como detener el escáner
//        super.onCleared()
//    }
//}
