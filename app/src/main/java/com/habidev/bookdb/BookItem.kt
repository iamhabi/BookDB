package com.habidev.bookdb

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookItem(
    @PrimaryKey(autoGenerate = true)
    private val id: Long,
    private val link: String?,
    private val title: String?,
    private val author: String?,
    private val imageUrl: String?,
    private val description: String?,
): Parcelable {
    constructor(parcel: Parcel) : this (
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    fun getId(): Long {
        return id
    }

    fun getLink(): String? {
        return link
    }

    fun getTitle(): String? {
        return title
    }

    fun getAuthor(): String? {
        return author
    }

    fun getImageUrl(): String? {
        return imageUrl
    }

    fun getDescription(): String? {
        return description
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(link)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(imageUrl)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookItem> {
        override fun createFromParcel(parcel: Parcel): BookItem {
            return BookItem(parcel)
        }

        override fun newArray(size: Int): Array<BookItem?> {
            return arrayOfNulls(size)
        }
    }
}