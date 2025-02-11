package com.ksj.sauruspang.ProfilePackage.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val name: String?,
    val birth: String?,
    @ColumnInfo(name = "selected_image") val selectedImage: Int?,
    val category: String?,
    val quizCategory: String?,
    val fruitsDayCount: Int = 1,
    val animalsDayCount: Int = 1,
    val colorsDayCount: Int = 1,
    val jobsDayCount: Int = 1
)