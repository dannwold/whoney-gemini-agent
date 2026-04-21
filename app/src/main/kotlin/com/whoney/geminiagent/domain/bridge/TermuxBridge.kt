package com.whoney.geminiagent.domain.bridge

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TermuxBridge(
    private val port: Int = 8080
) {
    private val client = OkHttpClient()
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun execute(command: String): String = withContext(Dispatchers.IO) {
        try {
            val payload = JSONObject().apply {
                put("command", command)
            }.toString()

            val request = Request.Builder()
                .url("http://127.0.0.1:$port/run")
                .post(payload.toRequestBody(mediaType))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext "Termux Error: ${response.code}"
                response.body?.string() ?: "Empty response"
            }
        } catch (e: Exception) {
            "Termux connection failed: ${e.message}"
        }
    }
}
