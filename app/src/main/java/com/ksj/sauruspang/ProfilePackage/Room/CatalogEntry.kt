package com.ksj.sauruspang.ProfilePackage.Room

import androidx.room.Entity

@Entity(tableName = "catalog_entries", primaryKeys = ["profileId", "answer"])
data class CatalogEntry(
    val profileId: Int,
    val answer: String,
    val image: ByteArray
)
