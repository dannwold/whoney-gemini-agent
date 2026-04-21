package com.whoney.geminiagent.domain.router

import com.whoney.geminiagent.core.Intent
import com.whoney.geminiagent.core.Plan
import com.whoney.geminiagent.core.ToolRouter
import com.whoney.geminiagent.domain.model.Tool
import com.whoney.geminiagent.domain.bridge.TermuxBridge

class ToolRouterImpl(
    private val termuxBridge: TermuxBridge
) : ToolRouter {
    private val tools = mutableMapOf<String, Tool>()

    init {
        registerTool(CalculatorTool())
        registerTool(SystemInfoTool())
    }

    private fun registerTool(tool: Tool) {
        tools[tool.name] = tool
    }

    override suspend fun classifyIntent(input: String): Intent {
        return when {
            input.startsWith("/termux") || input.startsWith("/exec") || input.startsWith("/shell") -> Intent("TERMUX")
            input.contains(Regex("\\d+ [+\\-*/] \\d+")) -> Intent("CALC")
            else -> Intent("GENERAL")
        }
    }

    override suspend fun createPlan(input: String, intent: Intent): Plan {
        return when (intent.type) {
            "TERMUX" -> Plan(requiresGemini = false, toolChain = listOf("TERMUX"))
            "CALC" -> Plan(requiresGemini = false, toolChain = listOf("CALCULATOR"))
            else -> Plan(requiresGemini = true, toolChain = emptyList())
        }
    }

    override suspend fun executePlan(plan: Plan): String {
        // In a real implementation, we'd pass the input through the chain
        // For this project, we'll assume the input is available or passed in
        return "Tool execution result"
    }

    suspend fun executePlanWithInput(plan: Plan, input: String): String {
        var lastOutput = ""
        for (toolName in plan.toolChain) {
            lastOutput = when (toolName) {
                "TERMUX" -> {
                    // Strip the prefix for termux commands
                    val cmd = input.removePrefix("/termux ").removePrefix("/exec ").removePrefix("/shell ")
                    termuxBridge.execute(cmd)
                }
                "CALCULATOR" -> tools["calculator"]?.execute(input) ?: "Error"
                else -> "Unknown tool"
            }
        }
        return lastOutput
    }
}

class CalculatorTool : Tool {
    override val name = "calculator"
    override val description = "Performs basic math"
    override suspend fun execute(input: String): String = "Calculation result placeholder"
}

class SystemInfoTool : Tool {
    override val name = "system_info"
    override val description = "Gets Android system info"
    override suspend fun execute(input: String): String = "Android 14, API 34"
}
