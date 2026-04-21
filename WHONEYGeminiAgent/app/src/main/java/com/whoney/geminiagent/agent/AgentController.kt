package com.whoney.geminiagent.agent

import android.util.Log
import com.whoney.geminiagent.memory.MemoryManager
import com.whoney.geminiagent.tools.ToolRouter
import com.whoney.geminiagent.gemini.GeminiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AgentController(
    private val memoryManager: MemoryManager,
    private val toolRouter: ToolRouter,
    private val geminiClient: GeminiClient
) {
    private val _state = MutableStateFlow(AgentState())
    val state: StateFlow<AgentState> = _state.asStateFlow()

    suspend fun processInput(input: String, onResponse: (String) -> Unit) {
        _state.update { it.copy(isProcessing = true, lastInput = input) }

        // 1. OBSERVE
        val context = observe(input)

        // 2. THINK
        val intent = think(input, context)

        // 3. PLAN
        val steps = plan(intent)

        // 4. ACT
        val result = act(steps, input, context, onResponse)

        // 5. REFLECT
        val reflection = reflect(result)

        // 6. STORE
        store(input, result, reflection)

        // 7. IMPROVE
        improve()

        _state.update { it.copy(isProcessing = false) }
    }

    private suspend fun observe(input: String): List<com.whoney.geminiagent.memory.model.MemoryEntry> {
        Log.d("AgentController", "Observing...")
        val memories = memoryManager.retrieveRelevantMemories(input)
        _state.update { it.copy(retrievedMemories = memories) }
        return memories
    }

    private fun think(input: String, context: List<com.whoney.geminiagent.memory.model.MemoryEntry>): String {
        Log.d("AgentController", "Thinking...")
        // Intent classification logic (could be Gemini-powered or rule-based)
        return "GENERIC_TASK"
    }

    private fun plan(intent: String): List<String> {
        Log.d("AgentController", "Planning...")
        return listOf("EXECUTE_GEMINI")
    }

    private suspend fun act(
        steps: List<String>,
        input: String,
        context: List<com.whoney.geminiagent.memory.model.MemoryEntry>,
        onResponse: (String) -> Unit
    ): String {
        Log.d("AgentController", "Acting...")
        var finalResult = ""

        // Simplified tool vs gemini execution
        if (input.startsWith("/")) {
            finalResult = toolRouter.routeAndExecute(input)
            onResponse(finalResult)
        } else {
            geminiClient.generateContentStream(input, context).collect { chunk ->
                finalResult += chunk
                onResponse(finalResult)
            }
        }
        return finalResult
    }

    private fun reflect(result: String): String {
        Log.d("AgentController", "Reflecting...")
        return "Quality check passed"
    }

    private suspend fun store(input: String, response: String, reflection: String) {
        Log.d("AgentController", "Storing...")
        memoryManager.saveMemory(input, response)
    }

    private fun improve() {
        Log.d("AgentController", "Improving...")
        // Logic to adjust future retrieval weights
    }
}
