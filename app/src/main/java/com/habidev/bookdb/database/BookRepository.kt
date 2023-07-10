package com.habidev.bookdb.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {
    val allBooks: Flow<List<BookItem>> = bookDao.getAll()

    @WorkerThread
    suspend fun insert(book: BookItem) {
        bookDao.insert(book)
    }

    @WorkerThread
    suspend fun update(book: BookItem) {
        bookDao.update(book)
    }

    @WorkerThread
    fun search(query: String): List<BookItem> = bookDao.search(query)
}