package com.habidev.bookdb.api.search

import com.habidev.bookdb.data.BookItem

data class SearchResult(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<BookItem>
)
