package com.habidev.bookdb.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.habidev.bookdb.activity.SomeInterface
import com.habidev.bookdb.databinding.CameraBinding
import com.habidev.bookdb.utils.Utils
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment: Fragment() {
    companion object {
        private const val TAG = "Barcode Reader"

        private const val TEST_BARCODE: String = "9791162996522"
    }

    private lateinit var viewBinding: CameraBinding

    private lateinit var cameraExecutor: ExecutorService

    private var imageCapture: ImageCapture? = null

    private var someInterface: SomeInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        someInterface = context as? SomeInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = CameraBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.btnCapture.setOnClickListener {
            scanBarcode()
//            scanBarcodeTest()
        }
    }

    override fun onResume() {
        super.onResume()

        if (Utils.isCamPermissionGranted(requireContext())) {
            startCamera()
        }
    }

    override fun onPause() {
        super.onPause()

        shutDownCameraExecutor()
    }

    override fun onStop() {
        super.onStop()

        shutDownCameraExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()

        shutDownCameraExecutor()
    }

    private fun showInfo(barcode: String) {
        someInterface?.showResultInfo(barcode)
    }

    private fun scanBarcode() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        val executor = ContextCompat.getMainExecutor(requireContext())

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(executor, ImageCapturedCallback())
    }

    private fun scanBarcodeTest() {
        showInfo(TEST_BARCODE)
    }

    private fun startCamera() {
        val context = context ?: return

        cameraExecutor = Executors.newSingleThreadExecutor()

        val executor = ContextCompat.getMainExecutor(context)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                    }

                imageCapture = ImageCapture.Builder().build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                } catch(exc: Exception) {
                    Log.e(TAG, "Use case binding failed", exc)
                }

            },
            executor
        )
    }

    private fun shutDownCameraExecutor() {
        if (this::cameraExecutor.isInitialized) {
            cameraExecutor.shutdown()
        }
    }

    inner class ImageCapturedCallback : ImageCapture.OnImageCapturedCallback() {
        @SuppressLint("UnsafeOptInUsageError")
        override fun onCaptureSuccess(imageProxy: ImageProxy) {
            super.onCaptureSuccess(imageProxy)

            val barcodeImage = imageProxy.image

            if (barcodeImage != null) {
                val fixedBarcodeImage = InputImage.fromMediaImage(barcodeImage, imageProxy.imageInfo.rotationDegrees)

                BarcodeScanning.getClient()
                    .process(fixedBarcodeImage)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            val rawValue = barcode.rawValue // barcode

                            Log.d(TAG, rawValue.toString())

                            if (rawValue != null) {
                                showInfo(rawValue)

                                shutDownCameraExecutor()

                                break
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FAIL", exception.toString())
                    }
            }
        }

        override fun onError(exception: ImageCaptureException) {
            super.onError(exception)

            Log.e(TAG, exception.toString())
        }
    }
}