package com.ksj.sauruspang.ProfilePackage.Room

import androidx.room.TypeConverter
import android.util.Base64

class Converters {
    @TypeConverter
    fun fromByteArrayList(byteArrayList: List<ByteArray>): List<String> {
        return byteArrayList.map { Base64.encodeToString(it, Base64.DEFAULT) }
    }

    @TypeConverter
    fun toByteArrayList(stringList: List<String>): List<ByteArray> {
        return stringList.map { Base64.decode(it, Base64.DEFAULT) }
    }

    @TypeConverter
    fun fromStringList(stringList: List<String>): String {
        return stringList.joinToString(",")  // 리스트를 쉼표로 연결된 문자열로 저장
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split(",")
    }
}