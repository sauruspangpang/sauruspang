package com.ksj.sauruspang.ProfilePackage.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatalogEntry(entry: CatalogEntry)

    @Query("SELECT * FROM catalog_entries WHERE profileId = :profileId")
    fun getCatalogEntriesForProfile(profileId: Int): Flow<List<CatalogEntry>>
}