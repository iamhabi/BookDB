package com.habidev.bookdb.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Utils {
    companion object {
        const val TAG = "BookDB"

        private const val PERMISSION_CAMERA = Manifest.permission.CAMERA
        const val PERMISSION_CAMERA_REQUEST_CODE = 50234

        fun isCamPermissionGranted(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                PERMISSION_CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun requestCameraPermission(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(PERMISSION_CAMERA),
                PERMISSION_CAMERA_REQUEST_CODE
            )
        }

        fun showKeyboard(context: Context, view: View) {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            inputMethodManager.showSoftInput(
                view,
                InputMethodManager.SHOW_IMPLICIT
            )
        }

        fun hideKeyboard(context: Context, view: View) {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}