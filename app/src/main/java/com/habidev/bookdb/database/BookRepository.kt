package com.habidev.bookdb.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {
    val allBooks: Flow<List<BookItem>> = bookDao.getAll()

    @WorkerThread
    suspend fun insert(bookItem: BookItem) {
        bookDao.insert(bookItem)
    }

    @WorkerThread
    suspend fun update(bookItem: BookItem) {
        bookDao.update(bookItem)
    }

    @WorkerThread
    suspend fun delete(bookItem: BookItem) {
        bookDao.delete(bookItem)
    }

    @WorkerThread
    fun search(query: String): List<BookItem> = bookDao.search(query)
}