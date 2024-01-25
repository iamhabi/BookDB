package com.habidev.bookdb.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.habidev.bookdb.R
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

    private lateinit var toastFailToReadBarcode: Toast

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

        toastFailToReadBarcode = Toast.makeText(requireContext(), R.string.fail_to_read_barcode, Toast.LENGTH_SHORT)

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

                val preview = initPreview()

                initImageCapture()

                try {
                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        this,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageCapture
                    )
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

    private fun initImageCapture() {
        imageCapture = ImageCapture.Builder().build()
    }

    private fun initPreview(): Preview {
        return Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
            }
    }

    private fun toastFailToReadBarcode() {
        toastFailToReadBarcode.run {
            cancel()
            show()
        }
    }

    inner class ImageCapturedCallback : ImageCapture.OnImageCapturedCallback() {
        @SuppressLint("UnsafeOptInUsageError")
        override fun onCaptureSuccess(imageProxy: ImageProxy) {
            super.onCaptureSuccess(imageProxy)

            imageProxy.image?.let { barcodeImage ->
                val fixedBarcodeImage = InputImage.fromMediaImage(barcodeImage, imageProxy.imageInfo.rotationDegrees)

                BarcodeScanning.getClient()
                    .process(fixedBarcodeImage)
                    .addOnSuccessListener { barcodes ->
                        if (barcodes.isNotEmpty()) {
                            barcodes.first().rawValue?.let {
                                showInfo(it)

                                shutDownCameraExecutor()
                            }
                        } else {
                            toastFailToReadBarcode()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FAIL", exception.toString())

                        toastFailToReadBarcode()
                    }
            }
        }

        override fun onError(exception: ImageCaptureException) {
            super.onError(exception)

            Log.e(TAG, exception.toString())

            toastFailToReadBarcode()
        }
    }
}