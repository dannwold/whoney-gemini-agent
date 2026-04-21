package com.whoney.geminiagent.memory

import androidx.room.*
import com.whoney.geminiagent.memory.model.MemoryEntry

@Dao
interface MemoryDao {
    @Query("SELECT * FROM memories")
    suspend fun getAll(): List<MemoryEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(memory: MemoryEntry)

    @Delete
    suspend fun delete(memory: MemoryEntry)
}
