package com.habidev.bookdb.api

import com.habidev.bookdb.data.BookItem
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface BookDBService {
    companion object {
        const val BASE_URL = ApiKey.BOOK_BASE_URL

        private const val LIST_JSON = "json"
        private const val ADD_BOOK = "add_book"
        private const val ADD_BOOK_JSON = "add_book_json"
    }

    @GET(LIST_JSON)
    fun getList(): Call<List<BookItem>>

    @Multipart
    @POST(ADD_BOOK)
    fun addBook(
        @Part("isbn") isbn: Long,
        @Part("link") link: String,
        @Part("title") title: String,
        @Part("author") author: String,
        @Part("image_url") imageUrl: String,
        @Part("description") description: String
    ): Call<ResponseBody>

    @POST(ADD_BOOK_JSON)
    fun addBookJson(@Body bookItem: RequestBody): Call<ResponseBody>
}