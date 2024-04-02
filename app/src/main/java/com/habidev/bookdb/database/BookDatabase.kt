package com.habidev.bookdb.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.habidev.bookdb.data.BookItem
import com.habidev.bookdb.data.GroupBookItem
import com.habidev.bookdb.data.GroupItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        BookItem::class,
        GroupItem::class,
        GroupBookItem::class
               ],
    version = 11,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(1, 2),
        AutoMigration(2, 3),
        AutoMigration(3, 4),
        AutoMigration(4, 5),
        AutoMigration(5, 6),
        AutoMigration(6, 7, BookDatabase.GroupTableNameChanged::class),
        AutoMigration(7, 8, BookDatabase.DeleteGroupFromBook::class),
        AutoMigration(8, 9, BookDatabase.RenameGroupToGroupId::class),
        AutoMigration(9, 10, BookDatabase.DeleteReadingAndOwningState::class),
        AutoMigration(10, 11),
    ]
)
abstract class BookDatabase: RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "books_database"
                )
                    .addCallback(BookDatabaseCallback(scope))
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }

    private class BookDatabaseCallback(
        private val scope: CoroutineScope
    ): Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { bookDatabase ->
                scope.launch {
                    populateDatabase(bookDatabase.bookDao())
                }
            }
        }

        suspend fun populateDatabase(bookDao: BookDao) {
            bookDao.deleteAllBooks()
            bookDao.deleteAllGroups()
            bookDao.deleteAllGroupBooks()
        }
    }

    @RenameTable(
        fromTableName = "book_groups",
        toTableName = BookDao.TABLE_NAME_GROUP
    )
    class GroupTableNameChanged : AutoMigrationSpec

    @DeleteColumn(
        tableName = BookDao.TABLE_NAME_BOOK,
        columnName = "group"
    )
    class DeleteGroupFromBook : AutoMigrationSpec

    @RenameColumn(
        tableName = BookDao.TABLE_NAME_GROUP_BOOKS,
        fromColumnName = "group",
        toColumnName = "groupId"
    )
    class RenameGroupToGroupId : AutoMigrationSpec

    @DeleteColumn.Entries(
        DeleteColumn(
            tableName = BookDao.TABLE_NAME_BOOK,
            columnName = "readingState"
        ),
        DeleteColumn(
            tableName = BookDao.TABLE_NAME_BOOK,
            columnName = "ownState"
        )
    )
    class DeleteReadingAndOwningState : AutoMigrationSpec
}