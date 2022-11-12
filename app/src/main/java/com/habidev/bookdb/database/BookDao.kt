package com.habidev.bookdb.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.habidev.bookdb.BookItem

@Dao
interface BookDao {
    @Insert
    fun insertBook(bookItem: BookItem)

    @Delete
    fun deleteBook(bookItem: BookItem)

    @Query("SELECT * FROM books WHERE id = :id")
    fun findBook(id: Int): BookItem

    @Query("SELECT * FROM books")
    fun getAll(): List<BookItem>
}