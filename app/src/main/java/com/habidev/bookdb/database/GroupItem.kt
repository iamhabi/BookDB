package com.habidev.bookdb.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = BookDao.TABLE_NAME_BOOK_GROUP)
data class GroupItem(
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

    companion object CREATOR : Parcelable.Creator<GroupItem> {
        override fun createFromParcel(parcel: Parcel): GroupItem {
            return GroupItem(parcel)
        }

        override fun newArray(size: Int): Array<GroupItem?> {
            return arrayOfNulls(size)
        }
    }
}