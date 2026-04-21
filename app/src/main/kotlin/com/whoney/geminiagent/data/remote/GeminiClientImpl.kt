package com.whoney.geminiagent.data.remote

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.whoney.geminiagent.core.GeminiClient
import com.whoney.geminiagent.domain.model.MemoryEntry
import kotlinx.coroutines.flow.collect

class GeminiClientImpl(
    private val apiKey: String
) : GeminiClient {
    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    override suspend fun generateResponse(
        input: String,
        context: List<MemoryEntry>,
        onUpdate: (String) -> Unit
    ): String {
        val prompt = buildPrompt(input, context)
        var fullResponse = ""

        model.generateContentStream(prompt).collect { chunk ->
            chunk.text?.let {
                fullResponse += it
                onUpdate(fullResponse)
            }
        }

        return fullResponse
    }

    private fun buildPrompt(input: String, memories: List<MemoryEntry>): String {
        val memoryContext = memories.joinToString("\n") {
            "User: ${it.userMessage}\nAssistant: ${it.assistantResponse}"
        }

        return """
            System: You are WHONEY, a high-performance AI agent.
            Use the following retrieved memories for context if relevant.

            Retrieved Context:
            ${memoryContext}

            Current User Message: ${input}
            Assistant:
        """.trimIndent()
    }
}
