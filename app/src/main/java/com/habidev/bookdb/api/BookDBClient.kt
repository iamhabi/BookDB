package com.habidev.bookdb.api

import android.util.ArrayMap
import android.util.Log
import com.habidev.bookdb.data.BookItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookDBClient {
    companion object {
        private const val TAG = "BookDBClient"

        fun getList(callback: (List<BookItem>) -> Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                val bookDBService = buildService()

                val call = bookDBService.getList()

                call.enqueue(object : Callback<List<BookItem>> {
                    override fun onResponse(
                        call: Call<List<BookItem>>,
                        response: Response<List<BookItem>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let(callback)
                        }
                    }

                    override fun onFailure(call: Call<List<BookItem>>, throwable: Throwable) {
                        Log.e(TAG, "Failed to get list", throwable)
                    }
                })
            }
        }

        fun addBook(bookItem: BookItem) {
            CoroutineScope(Dispatchers.IO).launch {
                val bookDBService = buildService()

                val call = bookDBService.addBook(
                    isbn = bookItem.isbn.toString(),
                    link = bookItem.link,
                    title = bookItem.title,
                    author = bookItem.author,
                    imageUrl = bookItem.imageUrl,
                    description = bookItem.description
                )

                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Log.d(TAG, "Success to add book")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, throwable: Throwable) {
                        Log.e(TAG, "Failed to add book", throwable)
                    }
                })
            }
        }

        fun addBookJson(bookItem: BookItem) {
            CoroutineScope(Dispatchers.IO).launch {
                val bookDBService = buildService()

                val bookItemJson = ArrayMap<String, String>()

                bookItemJson["author"] = bookItem.author
                bookItemJson["description"] = bookItem.description
                bookItemJson["image_url"] = bookItem.imageUrl
                bookItemJson["isbn"] = bookItem.isbn.toString()
                bookItemJson["link"] = bookItem.link
                bookItemJson["title"] = bookItem.title

                val jsonParams = ArrayMap<String, String>()

                jsonParams["contents"] = bookItemJson.toString()

                val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSONObject(jsonParams as Map<String, String>).toString())

                val callJson = bookDBService.addBookJson(body)

                callJson.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Log.d(TAG, "JSON Success to add book")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, throwable: Throwable) {
                        Log.e(TAG, "JSON Failed to add book", throwable)
                    }
                })
            }
        }

        private fun buildService(): BookDBService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BookDBService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(BookDBService::class.java)
        }
    }
}