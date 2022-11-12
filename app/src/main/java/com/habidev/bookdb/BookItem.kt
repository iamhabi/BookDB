package com.habidev.bookdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookItem(
    @PrimaryKey private var id: Int,
    private var imageUrl: String,
    private var title: String,
    private var author: String
) {
    fun getId(): Int {
        return id
    }

    fun getImageUrl(): String {
        return imageUrl
    }

    fun getTitle(): String {
        return title
    }

    fun getAuthor(): String {
        return author
    }
}