package com.ksj.sauruspang.ProfilePackage.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.io.File

@Database(entities = [User::class], version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "sauruspang.db"
                )
                    .addMigrations(MIGRATION_5_6)
                    .build().also { instance = it }
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE TABLE User_new (
            uid INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT,
            birth TEXT,
            selected_image INTEGER,
            category TEXT,
            quizCategory TEXT,
            dayCount INTEGER
            )
            """.trimIndent()
                )

                database.execSQL(
                    """
            INSERT INTO User_new (uid, name, birth, selected_image, category, quizCategory, dayCount)
            SELECT uid, name, birth, selected_image, category, quizCategory, dayCount FROM User
            """.trimIndent()
                )

                database.execSQL("DROP TABLE User")
                database.execSQL("ALTER TABLE User_new RENAME TO User")
            }
        }
    }
}
