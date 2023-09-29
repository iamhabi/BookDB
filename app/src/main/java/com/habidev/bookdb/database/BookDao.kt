package com.habidev.bookdb.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    companion object {
        const val TABLE_NAME = "books"
    }

    @Insert
    suspend fun insert(bookItem: BookItem)

    @Update
    suspend fun update(bookItem: BookItem)

    @Delete
    suspend fun delete(bookItem: BookItem)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()

    @Query("SELECT * FROM $TABLE_NAME WHERE isbn = :isbn")
    suspend fun searchByISBN(isbn: Long): BookItem

    @Query("SELECT * FROM $TABLE_NAME WHERE isbn LIKE '%' || :query || '%' OR title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    fun search(query: String): List<BookItem>

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): Flow<List<BookItem>>
}