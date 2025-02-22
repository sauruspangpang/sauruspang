package com.ksj.sauruspang.ProfilePackage.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [User::class, CatalogEntry::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun catalogEntryDao(): CatalogEntryDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "sauruspang.db"
                )
                    .addMigrations(MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                    .build().also { instance = it }
            }
        }

        // 마이그레이션 2 -> 3 : 기존 User 테이블을 새 구조(User_new)로 복사
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS User_new (
                        uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT,
                        birth TEXT,
                        selected_image TEXT,
                        score INTEGER NOT NULL DEFAULT 0,
                        category_day_status TEXT
                    )
                    """.trimIndent()
                )
                database.execSQL(
                    """
                    INSERT INTO User_new (uid, name, birth, selected_image, score, category_day_status)
                    SELECT uid, name, birth, selectedImage, 0, NULL FROM User
                    """.trimIndent()
                )
                database.execSQL("DROP TABLE User")
                database.execSQL("ALTER TABLE User_new RENAME TO User")
            }
        }

        // 마이그레이션 3 -> 4 : score와 category_day_status 컬럼 추가 (있지 않으면)
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                try {
                    database.execSQL("ALTER TABLE User ADD COLUMN score INTEGER NOT NULL DEFAULT 0")
                } catch (e: Exception) { }
                try {
                    database.execSQL("ALTER TABLE User ADD COLUMN category_day_status TEXT")
                } catch (e: Exception) { }
            }
        }

        // 마이그레이션 4 -> 5 : CatalogEntry 테이블 생성
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS catalog_entries (
                        profileId INTEGER NOT NULL,
                        answer TEXT NOT NULL,
                        image BLOB NOT NULL,
                        PRIMARY KEY (profileId, answer)
                    )
                    """.trimIndent()
                )
            }
        }
    }
}
