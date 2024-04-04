package com.habidev.bookdb.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.habidev.bookdb.data.SettingsItem
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    companion object {
        const val TABLE_NAME = "settings"
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(settingsItem: SettingsItem)

    @Update
    suspend fun update(settingsItem: SettingsItem)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()

    @Query("SELECT * FROM $TABLE_NAME LIMIT 1")
    fun getSettings(): Flow<SettingsItem>
}