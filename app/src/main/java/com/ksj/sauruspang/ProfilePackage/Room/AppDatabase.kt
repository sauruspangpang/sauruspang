package com.ksj.sauruspang.ProfilePackage.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.io.File

@Database(entities = [User::class], version = 3, exportSchema = false)
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
                    .addMigrations(MIGRATION_2_3)
                    .build().also { instance = it }
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 아래 SQL은 상황에 따라 달라질 수 있음.
                // 예를 들어, 기존 테이블의 컬럼 selectedImage를 새 컬럼 selected_image로 옮기기
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS User_new (
                        uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT,
                        birth TEXT,
                        selected_image INTEGER
                    )
                """.trimIndent())

                database.execSQL("""
                    INSERT INTO User_new (uid, name, birth, selected_image)
                    SELECT uid, name, birth, selectedImage FROM User
                """.trimIndent())

                database.execSQL("DROP TABLE User")
                database.execSQL("ALTER TABLE User_new RENAME TO User")
            }
        }
    }
}



