package com.habidev.bookdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.habidev.bookdb.BookItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert
    suspend fun insert(bookItem: BookItem)

    @Delete
    suspend fun delete(bookItem: BookItem)

    @Query("DELETE FROM books")
    suspend fun deleteAll()

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun find(id: Int): BookItem

    @Query("SELECT * FROM books")
    fun getAll(): Flow<List<BookItem>>
}