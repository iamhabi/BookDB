package com.habidev.bookdb.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = BookDao.TABLE_NAME_BOOK_GROUP, indices = [Index(value = ["title"], unique = true)])
data class BookGroupItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookGroupItem> {
        override fun createFromParcel(parcel: Parcel): BookGroupItem {
            return BookGroupItem(parcel)
        }

        override fun newArray(size: Int): Array<BookGroupItem?> {
            return arrayOfNulls(size)
        }
    }
}