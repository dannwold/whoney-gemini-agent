package com.whoney.geminiagent.core

import java.util.Random

class EmbeddingSystemImpl : EmbeddingSystem {
    private val cache = mutableMapOf<String, List<Float>>()
    private val random = Random()

    override suspend fun getEmbedding(text: String): List<Float> {
        return cache.getOrPut(text) {
            // Simplified: In a real app, use a local TFLite model or Gemini embedding API
            // For this project, we simulate a 384-dimensional vector (MiniLM size)
            List(384) { random.nextFloat() }
        }
    }
}
