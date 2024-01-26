package com.habidev.bookdb.api

import android.util.Log
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

class ApiClient {
    companion object {
        const val TAG = "ApiClient"

        // https://developers.naver.com/docs/serviceapi/search/book/book.md#%EC%B1%85
        const val BASE_URL = "https://openapi.naver.com/v1/search/"
        const val BOOK_SEARCH = "${BASE_URL}book..json?query="
        const val BOOK_DETAIL_SEARCH = "${BASE_URL}book_adv.json?query="

        interface OnResultListener {
            fun onResult(result: String)
        }

        fun search(query: String, detailSearch: Boolean = false, listener: OnResultListener) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val apiUrl = if (detailSearch) {
                        "${BOOK_DETAIL_SEARCH}$query"
                    } else {
                        "${BOOK_SEARCH}$query"
                    }

                    connect(apiUrl)?.run {
                        requestMethod = "GET"
                        setRequestProperty("X-Naver-Client-Id", ApiKey.CLIENT_ID)
                        setRequestProperty("X-Naver-Client-Secret", ApiKey.CLIENT_SECRET)

                        val result = if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                            readBody(inputStream)
                        } else { // 에러 발생
                            readBody(errorStream)
                        }

                        listener.onResult(result)
                    }
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