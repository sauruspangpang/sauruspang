package com.ksj.sauruspang.ProfilePackage.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAll(): Flow<List<User>>

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("UPDATE User SET dayCount = :newDayCount WHERE category = :categoryName")
    suspend fun updateDayCount(categoryName: String, newDayCount: Int)
}