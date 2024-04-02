package com.habidev.bookdb.data

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.habidev.bookdb.database.BookDao

@Entity(tableName = BookDao.TABLE_NAME_BOOK, indices = [Index(value = ["isbn"], unique = true)])
data class BookItem(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("isbn")
    val isbn: Long,
    @SerializedName("link")
    val link: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("description")
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