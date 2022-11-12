package com.habidev.bookdb

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookItem(
    @PrimaryKey private var id: Int,
    private var imageUrl: String,
    private var title: String,
    private var author: String,
    private var link: Uri
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

    fun getLink(): Uri {
        return link
    }
}