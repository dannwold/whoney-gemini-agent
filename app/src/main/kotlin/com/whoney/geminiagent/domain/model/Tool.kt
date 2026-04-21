package com.whoney.geminiagent.domain.model

interface Tool {
    val name: String
    val description: String
    suspend fun execute(input: String): String
}
