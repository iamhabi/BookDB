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
    var readingState: Int, // 0: Not read yet, 1: Reading, 2: Done
    var ownState: Int      // 0: Not own, 1: Wanna buy, 2: Own
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
        null,
        OWN_STATE_NOT_OWN,
        READ_STATE_NOT_YET
    )

    constructor(parcel: Parcel) : this (
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
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
        parcel.writeInt(ownState)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookItem> {
        const val READ_STATE_NOT_YET = 0
        const val READ_STATE_READING = 1
        const val READ_STATE_DONE    = 2

        val READ_STATE = arrayListOf(
            "Not yet",
            "Reading",
            "Done"
        )

        const val OWN_STATE_NOT_OWN   = 0
        const val OWN_STATE_WANNA_BUY = 1
        const val OWN_STATE_OWN       = 2

        val OWN_STATE = arrayListOf(
            "Not own",
            "Wanna buy",
            "Own"
        )

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BookItem {
            return BookItem(parcel)
        }

        override fun newArray(size: Int): Array<BookItem?> {
            return arrayOfNulls(size)
        }
    }
}