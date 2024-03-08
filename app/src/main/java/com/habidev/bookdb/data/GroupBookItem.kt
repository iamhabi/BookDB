package com.habidev.bookdb.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.habidev.bookdb.database.BookDao

@Entity(tableName = BookDao.TABLE_NAME_GROUP_BOOKS)
data class GroupBookItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var groupId: Int,
    var isbn: Long
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(groupId)
        parcel.writeLong(isbn)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GroupBookItem> {
        override fun createFromParcel(parcel: Parcel): GroupBookItem {
            return GroupBookItem(parcel)
        }

        override fun newArray(size: Int): Array<GroupBookItem?> {
            return arrayOfNulls(size)
        }
    }
}