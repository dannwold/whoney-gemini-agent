package com.whoney.geminiagent.bridge

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class TermuxBridge {
    private val client = OkHttpClient()
    private val port = 8080 // Example port

    suspend fun executeCommand(command: String): String {
        val url = "http://127.0.0.1:$port/run"
        val json = JSONObject().apply { put("command", command) }
        val body = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                response.body?.string() ?: "Empty response"
            }
        } catch (e: Exception) {
            "Error connecting to Termux: ${e.message}"
        }
    }
}
