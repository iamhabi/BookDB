package com.habidev.bookdb.database

import android.app.Application
import com.habidev.bookdb.database.BookRepository
import com.habidev.bookdb.database.BookRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BooksApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {
        BookRoomDatabase.getDatabase(this, applicationScope)
    }

    val repository by lazy {
        BookRepository(database.bookDao())
    }
}