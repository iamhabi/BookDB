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
final class BookRoomDatabase_AutoMigration_8_9_Impl extends Migration {
  private final AutoMigrationSpec callback = new BookDatabase.RenameGroupToGroupId();

  public BookRoomDatabase_AutoMigration_8_9_Impl() {
    super(8, 9);
  }

  @Override
  public void migrate(@NonNull final SupportSQLiteDatabase db) {
    db.execSQL("CREATE TABLE IF NOT EXISTS `_new_group_books` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `groupId` INTEGER NOT NULL, `isbn` INTEGER NOT NULL)");
    db.execSQL("INSERT INTO `_new_group_books` (`id`,`groupId`,`isbn`) SELECT `id`,`group`,`isbn` FROM `group_books`");
    db.execSQL("DROP TABLE `group_books`");
    db.execSQL("ALTER TABLE `_new_group_books` RENAME TO `group_books`");
    callback.onPostMigrate(db);
  }
}
