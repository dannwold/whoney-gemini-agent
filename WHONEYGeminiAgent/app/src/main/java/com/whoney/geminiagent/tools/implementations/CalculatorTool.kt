package com.whoney.geminiagent.tools.implementations

import com.whoney.geminiagent.tools.BaseTool

class CalculatorTool : BaseTool {
    override val name = "calculator"
    override suspend fun execute(args: String): String {
        // Very basic evaluator placeholder
        return try {
            "Result: ${args.toDouble()}" // Real impl would use an expression evaluator
        } catch (e: Exception) {
            "Error: Invalid expression"
        }
    }
}
