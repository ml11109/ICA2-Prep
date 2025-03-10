package com.example.ica2_prep.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

/*
In manifest:
<uses-permission android:name="android.permission.BODY_SENSORS"/>

To implement:
val shakeDetector = remember { ShakeDetector(context) {
    // Handle shake event
} }

DisposableEffect(Unit) {
    shakeDetector.startListening()
    onDispose { shakeDetector.stopListening() }
}
 */

class ShakeDetector(context: Context, private val onShake: () -> Unit) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var lastShakeTime = 0L

    fun startListening() {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]

            // Compute acceleration magnitude
            val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH

            // Detect shake
            if (acceleration > 12) { // Adjust threshold if needed
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastShakeTime > 1000) { // Prevent multiple triggers
                    lastShakeTime = currentTime
                    onShake()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}