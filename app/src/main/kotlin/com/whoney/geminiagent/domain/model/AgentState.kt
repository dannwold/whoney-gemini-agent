package com.whoney.geminiagent.domain.model

sealed class AgentState {
    object Idle : AgentState()
    object Observing : AgentState()
    object Thinking : AgentState()
    object Planning : AgentState()
    object Acting : AgentState()
    object Reflecting : AgentState()
    object Storing : AgentState()
    object Improving : AgentState()
    data class Error(val message: String) : AgentState()
}
