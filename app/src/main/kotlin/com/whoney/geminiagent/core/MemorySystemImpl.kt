package com.whoney.geminiagent.core

import com.whoney.geminiagent.domain.model.MemoryEntry
import kotlin.math.sqrt

class MemorySystemImpl(
    private val embeddingSystem: EmbeddingSystem
) : MemorySystem {
    private val memories = mutableListOf<MemoryEntry>()

    override suspend fun store(input: String, response: String, importance: Float) {
        val embedding = embeddingSystem.getEmbedding(input + response)
        val entry = MemoryEntry(
            userMessage = input,
            assistantResponse = response,
            embeddingVector = embedding,
            importanceScore = importance
        )
        memories.add(entry)
    }

    override suspend fun retrieve(input: String): List<MemoryEntry> {
        val queryEmbedding = embeddingSystem.getEmbedding(input)

        return memories.map { entry ->
            val similarity = cosineSimilarity(queryEmbedding, entry.embeddingVector)
            val recencyWeight = calculateRecencyWeight(entry.timestamp)
            val finalScore = (similarity * 0.5f) + (entry.importanceScore * 0.3f) + (recencyWeight * 0.2f)
            entry to finalScore
        }
        .sortedByDescending { it.second }
        .take(10)
        .map { it.first }
    }

    override suspend fun updateWeights(reflection: AgentController.Reflection) {
        // Placeholder for improvement logic
    }

    private fun cosineSimilarity(v1: List<Float>, v2: List<Float>): Float {
        var dotProduct = 0.0f
        var normA = 0.0f
        var normB = 0.0f
        for (i in v1.indices) {
            dotProduct += v1[i] * v2[i]
            normA += v1[i] * v1[i]
            normB += v2[i] * v2[i]
        }
        return if (normA > 0 && normB > 0) dotProduct / (sqrt(normA) * sqrt(normB)) else 0.0f
    }

    private fun calculateRecencyWeight(timestamp: Long): Float {
        val age = System.currentTimeMillis() - timestamp
        return (1.0f / (1.0f + age / 3600000f)) // Hourly decay
    }
}

interface EmbeddingSystem {
    suspend fun getEmbedding(text: String): List<Float>
}
