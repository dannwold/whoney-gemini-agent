package com.whoney.geminiagent.hardware

interface HardwareInterface {
    suspend fun scanBLE()
    suspend fun connectBLE(deviceId: String)
    suspend fun readSensor(sensorId: String): Double
    suspend fun executeDeviceCommand(command: String): Boolean
}

class HardwareScaffold : HardwareInterface {
    override suspend fun scanBLE() {
        // Scaffold only
    }

    override suspend fun connectBLE(deviceId: String) {
        // Scaffold only
    }

    override suspend fun readSensor(sensorId: String): Double {
        return 0.0
    }

    override suspend fun executeDeviceCommand(command: String): Boolean {
        return true
    }
}
