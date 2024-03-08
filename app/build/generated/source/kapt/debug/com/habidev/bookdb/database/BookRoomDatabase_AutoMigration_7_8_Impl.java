package com.habidev.bookdb.database;

import androidx.annotation.NonNull;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.lang.Override;
import java.lang.SuppressWarnings;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
final class BookRoomDatabase_AutoMigration_7_8_Impl extends Migration {
  private final AutoMigrationSpec callback = new BookDatabase.DeleteGroupFromBook();

  public BookRoomDatabase_AutoMigration_7_8_Impl() {
    super(7, 8);
  }

  @Override
  public void migrate(@NonNull final SupportSQLiteDatabase db) {
    db.execSQL("CREATE TABLE IF NOT EXISTS `group_books` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `group` TEXT NOT NULL, `isbn` INTEGER NOT NULL)");
    db.execSQL("CREATE TABLE IF NOT EXISTS `_new_books` (`isbn` INTEGER NOT NULL, `link` TEXT NOT NULL, `title` TEXT NOT NULL, `author` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `description` TEXT NOT NULL, `comment` TEXT, `readingState` INTEGER NOT NULL, `ownState` INTEGER NOT NULL, PRIMARY KEY(`isbn`))");
    db.execSQL("INSERT INTO `_new_books` (`isbn`,`link`,`title`,`author`,`imageUrl`,`description`,`comment`,`readingState`,`ownState`) SELECT `isbn`,`link`,`title`,`author`,`imageUrl`,`description`,`comment`,`readingState`,`ownState` FROM `books`");
    db.execSQL("DROP TABLE `books`");
    db.execSQL("ALTER TABLE `_new_books` RENAME TO `books`");
    db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_books_isbn` ON `books` (`isbn`)");
    callback.onPostMigrate(db);
  }
}
