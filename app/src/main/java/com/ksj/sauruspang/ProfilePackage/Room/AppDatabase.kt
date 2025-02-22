package com.ksj.sauruspang.ProfilePackage.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// 버전을 4로 올리고 새 컬럼(score, category_day_status)을 추가
@Database(entities = [User::class], version = 4, exportSchema = false)
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
                    .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
                    .build().also { instance = it }
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS User_new (
                uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT,
                birth TEXT,
                selected_image_path TEXT,
                catalog_image_path TEXT
            )
        """.trimIndent()
                )
                database.execSQL(
                    """
            INSERT INTO User_new (uid, name, birth, selected_image_path, catalog_image_path)
            SELECT uid, name, birth, selectedImage, catalogImage FROM User
        """.trimIndent()
                )
                database.execSQL("DROP TABLE User")
                database.execSQL("ALTER TABLE User_new RENAME TO User")
            }
        }

        // 마이그레이션 3 -> 4 : score와 category_day_status 컬럼 추가
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE User ADD COLUMN score INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE User ADD COLUMN category_day_status TEXT")
            }
        }
    }
}
