package com.habidev.bookdb

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookItem(
    @PrimaryKey(autoGenerate = true)
    var isbn: Long,
    var link: String,
    var title: String,
    var author: String,
    var imageUrl: String,
    var description: String,
    var comment: String?,
    var readingState: Int,
    var isOwning: Boolean,
    var wannaBuy: Boolean,
): Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this (
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readBoolean()
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(isbn)
        parcel.writeString(link)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(imageUrl)
        parcel.writeString(description)
        parcel.writeString(comment)
        parcel.writeInt(readingState)
        parcel.writeBoolean(isOwning)
        parcel.writeBoolean(wannaBuy)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookItem> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BookItem {
            return BookItem(parcel)
        }

        override fun newArray(size: Int): Array<BookItem?> {
            return arrayOfNulls(size)
        }
    }
}