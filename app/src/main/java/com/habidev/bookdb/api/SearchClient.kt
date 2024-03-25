package com.habidev.bookdb.api

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class SearchClient {
    companion object {
        const val TAG = "ApiClient"

        fun search(query: String, isDetailSearch: Boolean = false, callback: (String) -> Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                val retrofit = buildRetrofit()

                val searchService = retrofit.create(SearchService::class.java)

                val call = if (isDetailSearch) {
                    searchService.searchDetail(query)
                } else {
                    searchService.search(query)
                }

                call.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        response.body()?.let(callback)
                    }

                    override fun onFailure(call: Call<String>, throwable: Throwable) {
                        Log.e(TAG, call.toString(), throwable)
                    }
                })
            }
        }

        private fun buildRetrofit(): Retrofit {
            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor { chain ->
                val original = chain.request()

                val request = original.newBuilder()
                    .header("X-Naver-Client-Id", ApiKey.CLIENT_ID)
                    .header("X-Naver-Client-Secret", ApiKey.CLIENT_SECRET)
                    .method(original.method(), original.body())
                    .build()

                chain.proceed(request)
            }

            return Retrofit.Builder()
                .baseUrl(SearchService.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build()
        }
    }
}