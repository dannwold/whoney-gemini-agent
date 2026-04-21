package com.whoney.geminiagent.memory.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories")
data class MemoryEntry(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val userMessage: String,
    val assistantResponse: String,
    val embeddingJson: String, // Simplified storage of vector
    val importanceScore: Float,
    val usageCount: Int,
    val tags: String // Comma separated
)
