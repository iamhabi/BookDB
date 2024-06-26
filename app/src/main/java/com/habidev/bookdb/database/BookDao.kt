package com.habidev.bookdb.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.habidev.bookdb.data.BookItem
import com.habidev.bookdb.data.GroupBookItem
import com.habidev.bookdb.data.GroupItem
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

    @Insert
    suspend fun insertBookIntoGroup(groupBookItem: GroupBookItem)

    @Update
    suspend fun updateBook(bookItem: BookItem)

    @Update
    suspend fun updateGroup(groupItem: GroupItem)

    @Delete
    suspend fun deleteBook(bookItem: BookItem)

    @Delete
    suspend fun deleteGroup(groupItem: GroupItem)

    @Query("DELETE FROM $TABLE_NAME_GROUP_BOOKS WHERE isbn = :isbn AND groupId = :groupId")
    suspend fun deleteBookFromGroup(isbn: Long, groupId: Int)

    @Query("DELETE FROM $TABLE_NAME_BOOK")
    suspend fun deleteAllBooks()

    @Query("DELETE FROM $TABLE_NAME_GROUP")
    suspend fun deleteAllGroups()

    @Query("DELETE FROM $TABLE_NAME_GROUP_BOOKS")
    suspend fun deleteAllGroupBooks()

    @Query("SELECT * FROM $TABLE_NAME_BOOK WHERE isbn = :isbn")
    suspend fun searchByISBN(isbn: Long): BookItem

    @Query("SELECT * FROM $TABLE_NAME_BOOK WHERE isbn LIKE '%' || :query || '%' OR title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    fun searchBook(query: String): List<BookItem>

    @Query("SELECT * FROM $TABLE_NAME_BOOK")
    fun getBooksFlow(): Flow<List<BookItem>>

    @Query("SELECT * FROM $TABLE_NAME_BOOK WHERE isbn IN (SELECT isbn FROM $TABLE_NAME_GROUP_BOOKS WHERE groupId = :groupId)")
    fun getBooksByGroup(groupId: Int): Flow<List<BookItem>>

    @Query("SELECT * FROM $TABLE_NAME_GROUP")
    fun getGroupsFlow(): Flow<List<GroupItem>>
}