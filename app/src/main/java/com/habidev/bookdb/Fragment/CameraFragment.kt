package com.habidev.bookdb.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.habidev.bookdb.Activity.ResultActivity
import com.habidev.bookdb.databinding.CameraBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment: Fragment() {
    private val testBarcode: String = "9791162996522"

    private lateinit var viewBinding: CameraBinding

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

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
            takePhoto()
//            takePhotoTest()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onResume() {
        super.onResume()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                )
            }
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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        context?.let { it1 -> ContextCompat.checkSelfPermission(it1, it) } == PackageManager.PERMISSION_GRANTED
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Set up image capture listener, which is triggered after photo has been taken
        context?.let { ContextCompat.getMainExecutor(it) }?.let { it ->
            imageCapture.takePicture(
                it,
                object: ImageCapture.OnImageCapturedCallback() {
                    @SuppressLint("UnsafeOptInUsageError")
                    override fun onCaptureSuccess(imageProxy: ImageProxy) {
                        super.onCaptureSuccess(imageProxy)
                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                            BarcodeScanning.getClient()
                                .process(image)
                                .addOnSuccessListener { barcodes ->
                                    for (barcode in barcodes) {
                                        val rawValue = barcode.rawValue // value of barcode

                                        if (rawValue != null) {
                                            showInfo(rawValue)
                                        }
                                    }
                                }
                                .addOnFailureListener {
                                    Log.e("FAIL", it.toString())
                                }
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)

                        Log.e(TAG, exception.toString())
                    }
                }
            )
        }
    }

    private fun takePhotoTest() {
        showInfo(testBarcode)
    }

    private fun showInfo(barcode: String) {
        val intent = Intent(context, ResultActivity::class.java)

        intent.putExtra("barcode", barcode)

        startActivity(intent)
    }

    private fun startCamera() {
        val cameraProviderFuture = context?.let { ProcessCameraProvider.getInstance(it) }

        context?.let { ContextCompat.getMainExecutor(it) }?.let { it ->
            cameraProviderFuture?.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                    }

                imageCapture = ImageCapture.Builder()
                    .build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                } catch(exc: Exception) {
                    Log.e(TAG, "Use case binding failed", exc)
                }

            }, it)
        }
    }

    private fun shutDownCameraExecutor() {
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "Barcode Reader"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}