package com.habidev.bookdb.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    companion object {
        // https://developers.naver.com/docs/serviceapi/search/book/book.md#%EC%B1%85
        const val BASE_URL = "https://openapi.naver.com/v1/search/"
        const val BOOK_SEARCH = "book.json"
        const val BOOK_DETAIL_SEARCH = "book_adv.json"
    }

    @GET(BOOK_SEARCH)
    fun search(@Query("query") query: String): Call<SearchResult>

    @GET(BOOK_DETAIL_SEARCH)
    fun searchDetail(@Query("d_isbn") query: String): Call<SearchResult>
}