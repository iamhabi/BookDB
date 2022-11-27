package com.habidev.bookdb

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookItem(
    @PrimaryKey(autoGenerate = true)
    private var id: Int,
    private var imageUrl: String?,
    private var title: String?,
    private var author: String?,
    private var link: String?
): Parcelable {
    constructor(parcel: Parcel) : this (
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    fun getId(): Int {
        return id
    }

    fun getImageUrl(): String {
        return imageUrl!!
    }

    fun getTitle(): String {
        return title!!
    }

    fun getAuthor(): String {
        return author!!
    }

    fun getLink(): String {
        return link!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(imageUrl)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(link)
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