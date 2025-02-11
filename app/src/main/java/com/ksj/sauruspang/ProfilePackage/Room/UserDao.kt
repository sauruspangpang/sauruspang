package com.ksj.sauruspang.ProfilePackage.Room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAll(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("UPDATE User SET fruitsDayCount = :dayCount WHERE quizCategory = :quizCategory")
    fun updateFruitsDayCount(quizCategory: String, dayCount: Int)

    @Query("UPDATE User SET animalsDayCount = :dayCount WHERE quizCategory = :quizCategory")
    fun updateAnimalsDayCount(quizCategory: String, dayCount: Int)

    @Query("UPDATE User SET colorsDayCount = :dayCount WHERE quizCategory = :quizCategory")
    fun updateColorsDayCount(quizCategory: String, dayCount: Int)

    @Query("UPDATE User SET jobsDayCount = :dayCount WHERE quizCategory = :quizCategory")
    fun updateJobsDayCount(quizCategory: String, dayCount: Int)
}