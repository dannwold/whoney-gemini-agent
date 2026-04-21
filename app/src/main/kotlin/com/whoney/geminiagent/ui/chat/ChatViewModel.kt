package com.whoney.geminiagent.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whoney.geminiagent.core.AgentController
import com.whoney.geminiagent.domain.model.AgentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Message(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class ChatViewModel(
    private val agentController: AgentController
) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    val agentState: StateFlow<AgentState> = agentController.state

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val userMsg = Message(text, true)
        _messages.value = _messages.value + userMsg

        viewModelScope.launch {
            val assistantMsg = Message("", false)
            _messages.value = _messages.value + assistantMsg

            agentController.processInput(text) { updatedText ->
                updateLastMessage(updatedText)
            }
        }
    }

    private fun updateLastMessage(text: String) {
        val current = _messages.value.toMutableList()
        if (current.isNotEmpty()) {
            val last = current.last()
            if (!last.isUser) {
                current[current.size - 1] = last.copy(text = text)
                _messages.value = current
            }
        }
    }
}
