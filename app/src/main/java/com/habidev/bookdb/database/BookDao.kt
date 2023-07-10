package com.habidev.bookdb.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert
    suspend fun insert(bookItem: BookItem)

    @Delete
    suspend fun delete(bookItem: BookItem)

    @Query("DELETE FROM books")
    suspend fun deleteAll()

    @Query("SELECT * FROM books WHERE isbn = :isbn")
    suspend fun searchByISBN(isbn: Long): BookItem

    @Query("SELECT * FROM books WHERE isbn LIKE '%' || :query || '%' OR title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    suspend fun search(query: String): List<BookItem>

    @Query("SELECT * FROM books")
    fun getAll(): Flow<List<BookItem>>
}