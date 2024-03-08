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
final class BookRoomDatabase_AutoMigration_9_10_Impl extends Migration {
  private final AutoMigrationSpec callback = new BookDatabase.DeleteReadingAndOwningState();

  public BookRoomDatabase_AutoMigration_9_10_Impl() {
    super(9, 10);
  }

  @Override
  public void migrate(@NonNull final SupportSQLiteDatabase db) {
    db.execSQL("CREATE TABLE IF NOT EXISTS `_new_books` (`isbn` INTEGER NOT NULL, `link` TEXT NOT NULL, `title` TEXT NOT NULL, `author` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `description` TEXT NOT NULL, `comment` TEXT, PRIMARY KEY(`isbn`))");
    db.execSQL("INSERT INTO `_new_books` (`isbn`,`link`,`title`,`author`,`imageUrl`,`description`,`comment`) SELECT `isbn`,`link`,`title`,`author`,`imageUrl`,`description`,`comment` FROM `books`");
    db.execSQL("DROP TABLE `books`");
    db.execSQL("ALTER TABLE `_new_books` RENAME TO `books`");
    db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_books_isbn` ON `books` (`isbn`)");
    callback.onPostMigrate(db);
  }
}
