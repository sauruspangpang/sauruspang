package com.ksj.sauruspang.ProfilePackage.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catalog_entries")
data class CatalogEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val profileId: Int,
    val answer: String,
    val image: ByteArray
)
