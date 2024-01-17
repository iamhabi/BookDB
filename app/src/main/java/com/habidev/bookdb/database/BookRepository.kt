package com.habidev.bookdb.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {
    val allBooksFlow: Flow<List<BookItem>> = bookDao.getBooksFlow()

    @WorkerThread
    suspend fun insertBook(bookItem: BookItem) {
        bookDao.insertBook(bookItem)
    }

    @WorkerThread
    suspend fun updateBook(bookItem: BookItem) {
        bookDao.updateBook(bookItem)
    }

    @WorkerThread
    suspend fun deleteBook(bookItem: BookItem) {
        bookDao.deleteBook(bookItem)
    }

    @WorkerThread
    fun searchBook(query: String): List<BookItem> = bookDao.searchBook(query)
}