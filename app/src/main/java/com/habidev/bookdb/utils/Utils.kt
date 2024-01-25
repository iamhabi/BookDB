package com.habidev.bookdb.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.habidev.bookdb.ApiKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

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

        interface OnGetBookInfoListener {
            fun onGetBookInfo(result: String)
        }

        fun getBookInfo(query: String, listener: OnGetBookInfoListener) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val con = connect("${ApiKey.URL}$query")

                    con?.requestMethod = "GET"
                    con?.setRequestProperty("X-Naver-Client-Id", ApiKey.CLIENT_ID)
                    con?.setRequestProperty("X-Naver-Client-Secret", ApiKey.CLIENT_SECRET)

                    val responseCode = con!!.responseCode
                    val result = if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                        readBody(con.inputStream)
                    } else { // 에러 발생
                        readBody(con.errorStream)
                    }

                    listener.onGetBookInfo(result)
                } catch (e: Exception) {
                    Log.e("FAIL", e.toString())
                }
            }
        }

        private fun connect(apiUrl: String): HttpURLConnection? {
            return try {
                val url = URL(apiUrl)
                url.openConnection() as HttpURLConnection
            } catch (e: MalformedURLException) {
                throw RuntimeException("API URL이 잘못되었습니다. : $apiUrl", e)
            } catch (e: IOException) {
                throw RuntimeException("연결이 실패했습니다. : $apiUrl", e)
            }
        }

        private fun readBody(body: InputStream): String {
            val streamReader = InputStreamReader(body)
            try {
                BufferedReader(streamReader).use { lineReader ->
                    val responseBody = StringBuilder()
                    var line: String?
                    while (lineReader.readLine().also { line = it } != null) {
                        responseBody.append(line)
                    }
                    return responseBody.toString()
                }
            } catch (e: IOException) {
                throw RuntimeException("API 응답을 읽는데 실패했습니다.", e)
            }
        }
    }
}