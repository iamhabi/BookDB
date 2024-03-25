package com.habidev.bookdb.api

import android.util.Log
import com.habidev.bookdb.data.BookItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchClient {
    companion object {
        const val TAG = "SearchClient"

        fun search(query: String, callback: (List<BookItem>) -> Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                val retrofit = buildRetrofit()

                val searchService = retrofit.create(SearchService::class.java)

                val call = searchService.search(query)

                call.enqueue(object : Callback<SearchResult> {
                    override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                        if (response.isSuccessful) {
                            response.body()?.items?.let(callback)
                        }
                    }

                    override fun onFailure(call: Call<SearchResult>, throwable: Throwable) {
                        Log.e(TAG, call.toString(), throwable)
                    }
                })
            }
        }

        fun searchDetail(query: String, callback: (BookItem) -> Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                val retrofit = buildRetrofit()

                val searchService = retrofit.create(SearchService::class.java)

                val call = searchService.search(query)

                call.enqueue(object : Callback<SearchResult> {
                    override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                        if (response.isSuccessful) {
                            response.body()?.items?.first()?.let(callback)
                        }
                    }

                    override fun onFailure(call: Call<SearchResult>, throwable: Throwable) {
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
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
        }
    }
}