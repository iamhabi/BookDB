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
final class BookRoomDatabase_AutoMigration_6_7_Impl extends Migration {
  private final AutoMigrationSpec callback = new BookDatabase.GroupTableNameChanged();

  public BookRoomDatabase_AutoMigration_6_7_Impl() {
    super(6, 7);
  }

  @Override
  public void migrate(@NonNull final SupportSQLiteDatabase db) {
    db.execSQL("ALTER TABLE `book_groups` RENAME TO `groups`");
    callback.onPostMigrate(db);
  }
}
