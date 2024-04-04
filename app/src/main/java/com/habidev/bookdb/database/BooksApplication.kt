package com.habidev.bookdb.database

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BooksApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {
        BookDatabase.getDatabase(this, applicationScope)
    }

    val repository by lazy {
        BookRepository(database.bookDao())
    }

    val settingsRepository by lazy {
        SettingsRepository(database.settingsDao())
    }
}