package com.example.desafio.data.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HitsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: HitEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<HitEntity>): List<Long>

    @Query(value = """SELECT id FROM hits WHERE deleted=:deleted;""")
    fun getAllIdHitsDeleted(deleted: Boolean = true): List<String>
}