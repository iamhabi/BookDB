package com.habidev.bookdb.database

import androidx.annotation.WorkerThread
import com.habidev.bookdb.data.SettingsItem
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val settingsDao: SettingsDao
) {
    val settings: Flow<SettingsItem> = settingsDao.getSettings()

    @WorkerThread
    suspend fun init() {
        settingsDao.insert(SettingsItem(0, 1, 0))
    }

    @WorkerThread
    suspend fun update(settingsItem: SettingsItem) {
        settingsDao.update(settingsItem)
    }
}