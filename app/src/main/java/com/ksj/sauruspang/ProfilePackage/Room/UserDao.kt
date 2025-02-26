package com.ksj.sauruspang.ProfilePackage.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<User>>

    @Query("DELETE FROM users WHERE uid = :uid")
    suspend fun deleteById(uid: Int)
}
