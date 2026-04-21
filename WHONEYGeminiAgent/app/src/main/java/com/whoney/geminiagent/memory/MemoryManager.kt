package com.whoney.geminiagent.memory

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.whoney.geminiagent.memory.model.MemoryEntry
import java.util.*

class MemoryManager(
    private val memoryDao: MemoryDao,
    private val embeddingProvider: EmbeddingProvider
) {
    private val gson = Gson()

    suspend fun saveMemory(userMsg: String, aiResp: String) {
        val embedding = embeddingProvider.generateEmbedding(userMsg + aiResp)
        val entry = MemoryEntry(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            userMessage = userMsg,
            assistantResponse = aiResp,
            embeddingJson = gson.toJson(embedding),
            importanceScore = 1.0f,
            usageCount = 0,
            tags = ""
        )
        memoryDao.insert(entry)
    }

    suspend fun retrieveRelevantMemories(query: String, topK: Int = 5): List<MemoryEntry> {
        val queryEmbedding = embeddingProvider.generateEmbedding(query)
        val allMemories = memoryDao.getAll()

        return allMemories.map { memory ->
            val vectorType = object : TypeToken<List<Float>>() {}.type
            val vector: List<Float> = gson.fromJson(memory.embeddingJson, vectorType)
            val similarity = embeddingProvider.cosineSimilarity(queryEmbedding, vector)
            // Weighted retrieval (simplified: similarity * importance)
            val score = similarity * memory.importanceScore
            memory to score
        }
        .sortedByDescending { it.second }
        .take(topK)
        .map { it.first }
    }
}
