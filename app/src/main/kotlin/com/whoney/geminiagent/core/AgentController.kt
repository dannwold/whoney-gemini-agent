package com.whoney.geminiagent.core

import com.whoney.geminiagent.domain.model.AgentState
import com.whoney.geminiagent.domain.router.ToolRouterImpl
import com.whoney.geminiagent.domain.model.MemoryEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AgentController(
    private val memorySystem: MemorySystem,
    private val toolRouter: ToolRouter,
    private val geminiClient: GeminiClient
) {
    private val _state = MutableStateFlow<AgentState>(AgentState.Idle)
    val state: StateFlow<AgentState> = _state

    suspend fun processInput(input: String, onUpdate: (String) -> Unit) {
        // 1. OBSERVE
        _state.value = AgentState.Observing
        val context = memorySystem.retrieve(input)

        // 2. THINK
        _state.value = AgentState.Thinking
        val intent = toolRouter.classifyIntent(input)

        // 3. PLAN
        _state.value = AgentState.Planning
        val plan = toolRouter.createPlan(input, intent)

        // 4. ACT
        _state.value = AgentState.Acting
        val result = if (plan.requiresGemini) {
            geminiClient.generateResponse(input, context, onUpdate)
        } else {
            val toolResult =  (toolRouter as ToolRouterImpl).executePlanWithInput(plan, input)
            onUpdate(toolResult)
            toolResult
        }

        // 5. REFLECT
        _state.value = AgentState.Reflecting
        val reflection = reflectOnResult(input, result)

        // 6. STORE
        _state.value = AgentState.Storing
        memorySystem.store(input, result, reflection.importance)

        // 7. IMPROVE
        _state.value = AgentState.Improving
        memorySystem.updateWeights(reflection)

        _state.value = AgentState.Idle
    }

    private fun reflectOnResult(input: String, result: String): Reflection {
        // Simple reflection logic
        return Reflection(importance = 0.5f)
    }

    data class Reflection(val importance: Float)
}

interface MemorySystem {
    suspend fun store(input: String, response: String, importance: Float)
    suspend fun retrieve(input: String): List<MemoryEntry>
    suspend fun updateWeights(reflection: AgentController.Reflection)
}

interface ToolRouter {
    suspend fun classifyIntent(input: String): Intent
    suspend fun createPlan(input: String, intent: Intent): Plan
    suspend fun executePlan(plan: Plan): String
}

interface GeminiClient {
    suspend fun generateResponse(input: String, context: List<MemoryEntry>, onUpdate: (String) -> Unit): String
}

data class Intent(val type: String)
data class Plan(val requiresGemini: Boolean, val toolChain: List<String>)
