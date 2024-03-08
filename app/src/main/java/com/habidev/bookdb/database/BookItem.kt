package com.habidev.bookdb.database

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = BookDao.TABLE_NAME_BOOK, indices = [Index(value = ["isbn"], unique = true)])
data class BookItem(
    @PrimaryKey(autoGenerate = false)
    val isbn: Long,
    val link: String,
    val title: String,
    val author: String,
    val imageUrl: String,
    val description: String,
    var comment: String?,
): Parcelable {
    constructor(
        isbn: Long,
        link: String,
        title: String,
        author: String,
        imageUrl: String,
        description: String
    ): this(
        isbn,
        link,
        title,
        author,
        imageUrl,
        description,
        null
    )

    constructor(parcel: Parcel) : this (
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString()
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