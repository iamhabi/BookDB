package com.habidev.bookdb.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.habidev.bookdb.database.SettingsDao

@Entity(tableName = SettingsDao.TABLE_NAME)
data class SettingsItem(
    @PrimaryKey
    var id: Int,
    var isSortByTitle: Int,
    var isGird: Int
): Parcelable {
    constructor(parcel: Parcel): this (
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeInt(isSortByTitle)
        dest.writeInt(isGird)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SettingsItem> {
        override fun createFromParcel(source: Parcel): SettingsItem {
            return SettingsItem(source)
        }

        override fun newArray(size: Int): Array<SettingsItem?> {
            return arrayOfNulls(size)
        }

    }
}