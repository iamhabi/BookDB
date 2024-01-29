package com.habidev.bookdb.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {
    val allBooksFlow: Flow<List<BookItem>> = bookDao.getBooksFlow()

    val allGroupsFlow: Flow<List<GroupItem>> = bookDao.getGroupsFlow()

    /**
     * DB에 BookItem을 insert하고 성공 여부를 반환한다.
     */
    @WorkerThread
    suspend fun insertBook(bookItem: BookItem): Boolean {
        val result = bookDao.insertBook(bookItem)

        return result != -1L
    }

    @WorkerThread
    suspend fun insertGroup(groupItem: GroupItem) {
        bookDao.insertGroup(groupItem)
    }

    @WorkerThread
    suspend fun updateBook(bookItem: BookItem) {
        bookDao.updateBook(bookItem)
    }

    @WorkerThread
    suspend fun updateGroup(groupItem: GroupItem) {
        bookDao.updateGroup(groupItem)
    }

    @WorkerThread
    suspend fun deleteBook(bookItem: BookItem) {
        bookDao.deleteBook(bookItem)
    }

    @WorkerThread
    suspend fun deleteGroup(groupItem: GroupItem) {
        bookDao.deleteGroup(groupItem)
    }

    @WorkerThread
    fun searchBook(query: String): List<BookItem> = bookDao.searchBook(query)

    @WorkerThread
    fun getBooksByGroup(groupItem: GroupItem): List<BookItem> = bookDao.getBooksByGroup(groupItem.title)
}