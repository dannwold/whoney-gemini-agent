package com.whoney.geminiagent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.whoney.geminiagent.agent.AgentController
import com.whoney.geminiagent.bridge.TermuxBridge
import com.whoney.geminiagent.gemini.GeminiClient
import com.whoney.geminiagent.memory.AppDatabase
import com.whoney.geminiagent.memory.EmbeddingProvider
import com.whoney.geminiagent.memory.MemoryManager
import com.whoney.geminiagent.tools.ToolRouter
import com.whoney.geminiagent.ui.chat.ChatScreen
import com.whoney.geminiagent.ui.chat.ChatViewModel
import com.whoney.geminiagent.ui.theme.WHONEYTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DI logic (manual for this example)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "whoney-db"
        ).build()

        val memoryManager = MemoryManager(db.memoryDao(), EmbeddingProvider())
        val termuxBridge = TermuxBridge()
        val toolRouter = ToolRouter(termuxBridge)
        val geminiClient = GeminiClient(apiKey = "YOUR_API_KEY") // Placeholder

        val agentController = AgentController(memoryManager, toolRouter, geminiClient)
        val viewModel = ChatViewModel(agentController)

        setContent {
            WHONEYTheme {
                ChatScreen(viewModel)
            }
        }
    }
}
