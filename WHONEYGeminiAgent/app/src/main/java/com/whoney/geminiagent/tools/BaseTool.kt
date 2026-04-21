package com.whoney.geminiagent.tools

interface BaseTool {
    val name: String
    suspend fun execute(args: String): String
}
