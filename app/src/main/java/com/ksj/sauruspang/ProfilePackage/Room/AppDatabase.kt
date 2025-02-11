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

        val MIGRATION_2_3 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 새 컬럼 selected_image_path, catalog_image_path를 추가
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

                // 기존 데이터를 새로운 테이블로 이동 (selectedImage -> selected_image_path, catalogImage -> catalog_image_path)
                database.execSQL(
                    """
            INSERT INTO User_new (uid, name, birth, selected_image_path, catalog_image_path)
            SELECT uid, name, birth, selectedImage, catalogImage FROM User
        """.trimIndent()
                )

                // 기존 테이블 삭제 후 새 테이블로 변경
                database.execSQL("DROP TABLE User")
                database.execSQL("ALTER TABLE User_new RENAME TO User")
            }
        }
    }
}




