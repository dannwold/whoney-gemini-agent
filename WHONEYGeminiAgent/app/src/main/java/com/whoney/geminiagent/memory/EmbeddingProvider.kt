package com.whoney.geminiagent.memory

import com.google.gson.Gson
import kotlin.math.sqrt

class EmbeddingProvider {
    private val gson = Gson()

    fun generateEmbedding(text: String): List<Float> {
        // Placeholder for real embedding generation (e.g. MiniLM or API)
        // For now, generate a pseudo-random deterministic vector based on text
        val hash = text.hashCode().toFloat()
        return List(128) { i -> (hash + i) % 100 / 100f }
    }

    fun cosineSimilarity(v1: List<Float>, v2: List<Float>): Float {
        var dotProduct = 0.0f
        var normA = 0.0f
        var normB = 0.0f
        for (i in v1.indices) {
            dotProduct += v1[i] * v2[i]
            normA += v1[i] * v1[i]
            normB += v2[i] * v2[i]
        }
        return dotProduct / (sqrt(normA) * sqrt(normB))
    }
}
