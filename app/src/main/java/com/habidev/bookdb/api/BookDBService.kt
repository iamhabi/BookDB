package com.habidev.bookdb.api

import com.habidev.bookdb.data.BookItem
import retrofit2.Call
import retrofit2.http.GET

interface BookDBService {
    companion object {
        const val BASE_URL = ApiKey.BOOK_BASE_URL

        private const val LIST_JSON = "json"
    }

    @GET(LIST_JSON)
    fun getList(): Call<List<BookItem>>
}