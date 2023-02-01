package com.habidev.bookdb.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.habidev.bookdb.BookItem

@Dao
interface BookDao {
    @Insert
    suspend fun insertBook(bookItem: BookItem)

    @Delete
    suspend fun deleteBook(bookItem: BookItem)

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun findBook(id: Int): BookItem

    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<BookItem>
}