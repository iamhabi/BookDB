package com.habidev.bookdb.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    companion object {
        const val TABLE_NAME_BOOK = "books"
        const val TABLE_NAME_GROUP = "groups"
        const val TABLE_NAME_GROUP_BOOKS = "group_books"
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBook(bookItem: BookItem): Long

    @Insert
    suspend fun insertGroup(groupItem: GroupItem)

    @Update
    suspend fun updateBook(bookItem: BookItem)

    @Update
    suspend fun updateGroup(groupItem: GroupItem)

    @Delete
    suspend fun deleteBook(bookItem: BookItem)

    @Delete
    suspend fun deleteGroup(groupItem: GroupItem)

    @Query("DELETE FROM $TABLE_NAME_BOOK")
    suspend fun deleteAllBooks()

    @Query("DELETE FROM $TABLE_NAME_GROUP")
    suspend fun deleteAllGroups()

    @Query("SELECT * FROM $TABLE_NAME_BOOK WHERE isbn = :isbn")
    suspend fun searchByISBN(isbn: Long): BookItem

    @Query("SELECT * FROM $TABLE_NAME_BOOK WHERE isbn LIKE '%' || :query || '%' OR title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    fun searchBook(query: String): List<BookItem>

    @Query("SELECT * FROM $TABLE_NAME_BOOK")
    fun getBooksFlow(): Flow<List<BookItem>>

    @Query("SELECT * FROM $TABLE_NAME_BOOK WHERE `group` = :group")
    fun getBooksByGroup(group: String): List<BookItem>

    @Query("SELECT * FROM $TABLE_NAME_GROUP")
    fun getGroupsFlow(): Flow<List<GroupItem>>
}