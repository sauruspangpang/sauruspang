package com.ksj.sauruspang.ProfilePackage.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val name: String?,
    val birth: String?,
    val selectedImage: Int?,
    val score: Int,
    // 직렬화된 categoryDayStatus (예: "Math:1,Science:1,History:1,Language:1")
    val categoryDayStatus: String?
)
