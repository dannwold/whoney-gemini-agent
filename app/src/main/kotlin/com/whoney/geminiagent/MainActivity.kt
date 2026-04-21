package com.whoney.geminiagent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.whoney.geminiagent.core.*
import com.whoney.geminiagent.data.remote.GeminiClientImpl
import com.whoney.geminiagent.domain.bridge.TermuxBridge
import com.whoney.geminiagent.domain.router.ToolRouterImpl
import com.whoney.geminiagent.ui.chat.ChatViewModel
import com.whoney.geminiagent.ui.chat.Message

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Manual DI for core systems
        val embeddingSystem = EmbeddingSystemImpl()
        val memorySystem = MemorySystemImpl(embeddingSystem)
        val termuxBridge = TermuxBridge()
        val toolRouter = ToolRouterImpl(termuxBridge)
        val geminiClient = GeminiClientImpl(apiKey = "YOUR_API_KEY")
        val agentController = AgentController(memorySystem, toolRouter, geminiClient)

        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatScreen(agentController)
                }
            }
        }
    }
}

@Composable
fun ChatScreen(agentController: AgentController) {
    // In a real app, use a proper Factory or Hilt
    val viewModel = remember { ChatViewModel(agentController) }
    val messages by viewModel.messages.collectAsState()
    val state by viewModel.agentState.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // State Indicator
        Text(
            text = "State: ${state.javaClass.simpleName}",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.labelSmall
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            reverseLayout = false
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask WHONEY...") }
            )
            Button(
                onClick = {
                    viewModel.sendMessage(inputText)
                    inputText = ""
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Send")
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val color = if (message.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = alignment
    ) {
        Surface(
            color = color,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = Color.White
            )
        }
    }
}
