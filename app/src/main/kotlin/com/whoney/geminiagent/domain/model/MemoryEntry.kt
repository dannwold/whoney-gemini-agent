package com.whoney.geminiagent.domain.model

import java.util.UUID

data class MemoryEntry(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val userMessage: String,
    val assistantResponse: String,
    val embeddingVector: List<Float>,
    val importanceScore: Float, // 0.0 - 1.0
    val usageCount: Int = 0,
    val tags: List<String> = emptyList()
)
