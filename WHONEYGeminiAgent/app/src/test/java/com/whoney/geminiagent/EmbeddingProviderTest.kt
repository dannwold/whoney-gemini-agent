package com.whoney.geminiagent

import com.whoney.geminiagent.memory.EmbeddingProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EmbeddingProviderTest {
    private val provider = EmbeddingProvider()

    @Test
    fun testGenerateEmbeddingLength() {
        val embedding = provider.generateEmbedding("test")
        assertEquals(128, embedding.size)
    }

    @Test
    fun testCosineSimilarityIdentical() {
        val v = listOf(1.0f, 0.0f, 0.0f)
        val similarity = provider.cosineSimilarity(v, v)
        assertEquals(1.0f, similarity, 0.001f)
    }

    @Test
    fun testCosineSimilarityOrthogonal() {
        val v1 = listOf(1.0f, 0.0f, 0.0f)
        val v2 = listOf(0.0f, 1.0f, 0.0f)
        val similarity = provider.cosineSimilarity(v1, v2)
        assertEquals(0.0f, similarity, 0.001f)
    }
}
