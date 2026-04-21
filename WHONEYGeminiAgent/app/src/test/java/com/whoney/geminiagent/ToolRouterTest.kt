package com.whoney.geminiagent

import com.whoney.geminiagent.bridge.TermuxBridge
import com.whoney.geminiagent.tools.ToolRouter
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class ToolRouterTest {
    private val toolRouter = ToolRouter(TermuxBridge())

    @Test
    fun testSysTool() = runBlocking {
        val result = toolRouter.routeAndExecute("/sys")
        assertTrue(result.contains("Device:"))
    }

    @Test
    fun testCalcTool() = runBlocking {
        val result = toolRouter.routeAndExecute("/calc 10")
        assertTrue(result.contains("Result: 10.0"))
    }
}
