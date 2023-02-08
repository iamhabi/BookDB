package com.habidev.bookdb.dao

import androidx.annotation.WorkerThread
import com.habidev.bookdb.BookItem
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {
    val allBooks: Flow<List<BookItem>> = bookDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(book: BookItem) {
        bookDao.insert(book)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun search(query: String): List<BookItem> {
        return bookDao.search(query)
    }
}