package com.whoney.geminiagent.core

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class MemorySystemTest {

    class FakeEmbeddingSystem : EmbeddingSystem {
        override suspend fun getEmbedding(text: String): List<Float> {
            return if (text.contains("apple")) {
                List(384) { 1.0f }
            } else {
                List(384) { 0.0f }
            }
        }
    }

    @Test
    fun testRetrieval() = runBlocking {
        val memorySystem = MemorySystemImpl(FakeEmbeddingSystem())

        memorySystem.store("I like apples", "Indeed they are good", 0.8f)
        memorySystem.store("The sky is blue", "Yes it is", 0.5f)

        val results = memorySystem.retrieve("Tell me about apple")

        assertEquals(2, results.size)
        assertEquals("I like apples", results[0].userMessage)
    }
}
