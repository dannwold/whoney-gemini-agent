package com.whoney.geminiagent.tools.implementations

import android.os.Build
import com.whoney.geminiagent.tools.BaseTool

class SystemInfoTool : BaseTool {
    override val name = "system_info"
    override suspend fun execute(args: String): String {
        return "Device: ${Build.DEVICE}, Model: ${Build.MODEL}, OS: ${Build.VERSION.RELEASE}"
    }
}
