package com.habidev.bookdb.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children

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

        fun showKeyboard(activity: Activity, view: View) {
            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }

        fun closeKeyboard(activity: Activity) {
            activity.currentFocus?.let { view ->
                val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        fun setUpEditTextCloseKeyboard(activity: Activity, view: View) {
            if (view !is EditText) {
                view.setOnTouchListener { _, motionEvent ->
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {
                            closeKeyboard(activity)
                            activity.currentFocus?.clearFocus()
                        }

                        MotionEvent.ACTION_UP -> {
                            view.performClick()
                        }
                    }

                    false
                }
            }

            if (view is ViewGroup) {
                for (childView in view.children) {
                    setUpEditTextCloseKeyboard(activity, childView)
                }
            }
        }
    }
}