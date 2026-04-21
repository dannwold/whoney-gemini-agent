package com.whoney.geminiagent.memory

import androidx.room.Database
import androidx.room.RoomDatabase
import com.whoney.geminiagent.memory.model.MemoryEntry

@Database(entities = [MemoryEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memoryDao(): MemoryDao
}
