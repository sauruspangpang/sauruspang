package com.ksj.sauruspang.ProfilePackage.Room

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "catalog_entries",
    primaryKeys = ["profileId", "answer"]
)
data class CatalogEntry(
    val profileId: Int,
    val answer: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray
)
