package com.whoney.geminiagent.hardware

interface HardwareInterface {
    fun initialize()
    fun scanDevices()
    fun readSensorData(): String
}

class BleScanner : HardwareInterface {
    override fun initialize() {
        // BLE init logic scaffold
    }

    override fun scanDevices() {
        // BLE scan logic scaffold
    }

    override fun readSensorData(): String = "BLE Sensor Data placeholder"
}

class SensorReader : HardwareInterface {
    override fun initialize() {
        // System sensor init logic scaffold
    }

    override fun scanDevices() {
        // Not applicable for internal sensors
    }

    override fun readSensorData(): String = "Accelerometer: 0.0, 0.0, 0.0"
}
