package com.whoney.geminiagent.gemini

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.whoney.geminiagent.memory.model.MemoryEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GeminiClient(apiKey: String) {
    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    fun generateContentStream(
        input: String,
        memories: List<MemoryEntry>
    ): Flow<String> {
        val prompt = buildPrompt(input, memories)
        return model.generateContentStream(prompt).map { it.text ?: "" }
    }

    private fun buildPrompt(input: String, memories: List<MemoryEntry>): String {
        val memoryContext = if (memories.isNotEmpty()) {
            "RELEVANT MEMORIES:\n" + memories.joinToString("\n") {
                "- User: ${it.userMessage}\n  AI: ${it.assistantResponse}"
            }
        } else {
            "No relevant memories found."
        }

        return """
            SYSTEM: You are WHONEY, a high-performance Android AI agent.
            Use the provided memory context to maintain continuity.
            Be concise and technical.

            ${memoryContext}

            USER: ${input}
            AI:
        """.trimIndent()
    }
}
