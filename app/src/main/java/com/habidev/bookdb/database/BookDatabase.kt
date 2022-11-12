package com.habidev.bookdb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.habidev.bookdb.BookItem

@Database(entities = [BookItem::class], version = 1)
abstract class BookDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDao
}