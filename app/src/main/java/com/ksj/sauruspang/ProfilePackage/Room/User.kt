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
    val quizCategory: String?, // 새로운 필드 추가
//    val quizScore: Int?, // 새로운 필드 추가
    val dayCount: Int?, // 새로운 필드 추가
//    val userprofile: Int?
)