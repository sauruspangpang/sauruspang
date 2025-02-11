package com.ksj.sauruspang.ProfilePackage.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [User::class], version = 7, exportSchema = false)
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
                    .addMigrations(MIGRATION_6_7)
                    .build().also { instance = it }
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE User ADD COLUMN fruitsDayCount INTEGER DEFAULT 1")
                database.execSQL("ALTER TABLE User ADD COLUMN animalsDayCount INTEGER DEFAULT 1")
                database.execSQL("ALTER TABLE User ADD COLUMN colorsDayCount INTEGER DEFAULT 1")
                database.execSQL("ALTER TABLE User ADD COLUMN jobsDayCount INTEGER DEFAULT 1")
            }
        }
    }
}