package com.habidev.bookdb.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {
    val allBooksFlow: Flow<List<BookItem>> = bookDao.getBooksFlow()

    val allGroupsFlow: Flow<List<BookGroupItem>> = bookDao.getGroupsFlow()

    @WorkerThread
    suspend fun insertBook(bookItem: BookItem) {
        bookDao.insertBook(bookItem)
    }

    @WorkerThread
    suspend fun insertGroup(groupItem: BookGroupItem) {
        bookDao.insertGroup(groupItem)
    }

    @WorkerThread
    suspend fun updateBook(bookItem: BookItem) {
        bookDao.updateBook(bookItem)
    }

    @WorkerThread
    suspend fun updateGroup(groupItem: BookGroupItem) {
        bookDao.updateGroup(groupItem)
    }

    @WorkerThread
    suspend fun deleteBook(bookItem: BookItem) {
        bookDao.deleteBook(bookItem)
    }

    @WorkerThread
    suspend fun deleteGroup(groupItem: BookGroupItem) {
        bookDao.deleteGroup(groupItem)
    }

    @WorkerThread
    fun searchBook(query: String): List<BookItem> = bookDao.searchBook(query)

    @WorkerThread
    fun getBooksByGroup(groupItem: BookGroupItem): List<BookItem> = bookDao.getBooksByGroup(groupItem.title)
}