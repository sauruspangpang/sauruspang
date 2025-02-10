package com.ksj.sauruspang.ProfilePackage.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val name: String?,
    val birth: String?,
    @ColumnInfo(name = "selected_image") val selectedImage: Int?,
    @ColumnInfo(name = "cleared_image") val clearedImage: List<String>?, // ByteArray를 String으로 변환해서 저장
    @ColumnInfo(name = "cleared_words") val clearedWords: List<String>? // List<String>을 String으로 변환해서 저장
)