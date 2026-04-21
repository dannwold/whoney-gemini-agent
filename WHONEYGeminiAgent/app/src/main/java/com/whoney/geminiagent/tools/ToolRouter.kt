package com.whoney.geminiagent.tools

import com.whoney.geminiagent.bridge.TermuxBridge
import com.whoney.geminiagent.tools.implementations.*

class ToolRouter(
    private val termuxBridge: TermuxBridge
) {
    private val tools = mapOf<String, BaseTool>(
        "calc" to CalculatorTool(),
        "sys" to SystemInfoTool()
    )

    suspend fun routeAndExecute(input: String): String {
        return when {
            input.startsWith("/termux") || input.startsWith("/exec") || input.startsWith("/shell") -> {
                val cmd = input.substringAfter(" ").trim()
                termuxBridge.executeCommand(cmd)
            }
            input.startsWith("/") -> {
                val toolName = input.substring(1).substringBefore(" ")
                val args = input.substringAfter(" ").trim()
                tools[toolName]?.execute(args) ?: "Unknown tool: $toolName"
            }
            else -> "Not a tool command"
        }
    }
}
