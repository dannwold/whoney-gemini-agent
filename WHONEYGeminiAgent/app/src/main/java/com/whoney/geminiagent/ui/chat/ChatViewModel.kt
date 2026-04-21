package com.whoney.geminiagent.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whoney.geminiagent.agent.AgentController
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class Message(val content: String, val isUser: Boolean, val isStreaming: Boolean = false)

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val inputText: String = "",
    val isProcessing: Boolean = false
)

class ChatViewModel(private val agentController: AgentController) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onInputChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun sendMessage() {
        val input = _uiState.value.inputText
        if (input.isBlank()) return

        val userMsg = Message(input, true)
        _uiState.update { it.copy(
            messages = it.messages + userMsg,
            inputText = "",
            isProcessing = true
        ) }

        viewModelScope.launch {
            val aiMsgPlaceholder = Message("", false, true)
            _uiState.update { it.copy(messages = it.messages + aiMsgPlaceholder) }

            agentController.processInput(input) { chunk ->
                _uiState.update { state ->
                    val updatedMessages = state.messages.toMutableList()
                    val lastIndex = updatedMessages.lastIndex
                    updatedMessages[lastIndex] = Message(chunk, false, true)
                    state.copy(messages = updatedMessages)
                }
            }

            _uiState.update { state ->
                val updatedMessages = state.messages.toMutableList()
                val lastIndex = updatedMessages.lastIndex
                updatedMessages[lastIndex] = updatedMessages[lastIndex].copy(isStreaming = false)
                state.copy(messages = updatedMessages, isProcessing = false)
            }
        }
    }
}
