package com.whoney.geminiagent.agent

import com.whoney.geminiagent.memory.model.MemoryEntry

data class AgentState(
    val lastInput: String = "",
    val currentTask: String = "",
    val retrievedMemories: List<MemoryEntry> = emptyList(),
    val plan: List<String> = emptyList(),
    val currentOutput: String = "",
    val isProcessing: Boolean = false
)
