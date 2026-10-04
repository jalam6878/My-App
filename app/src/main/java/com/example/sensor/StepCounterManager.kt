package com.example.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.sqrt

enum class WalkingSessionState {
    IDLE,
    WALKING,
    PAUSED
}

class StepCounterManager(private val context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val stepCounterSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    private val stepDetectorSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    private val accelerometerSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val _isSensorAvailable = MutableStateFlow(
        stepCounterSensor != null || stepDetectorSensor != null || accelerometerSensor != null
    )
    val isSensorAvailable: StateFlow<Boolean> = _isSensorAvailable.asStateFlow()

    private val _sensorName = MutableStateFlow(
        when {
            stepCounterSensor != null -> stepCounterSensor.name
            stepDetectorSensor != null -> stepDetectorSensor.name
            accelerometerSensor != null -> "Accelerometer Dynamic Motion Sensor"
            else -> "Step Sensor Unavailable"
        }
    )
    val sensorName: StateFlow<String> = _sensorName.asStateFlow()

    // Callback on every step
    var onMetricsUpdated: ((
        dailySteps: Int,
        dailyDistKm: Float,
        dailyCal: Int,
        dailyActiveMin: Int,
        sessionSteps: Int,
        sessionDistKm: Float,
        sessionCal: Int
    ) -> Unit)? = null

    // Cumulative sensor offset management
    private var initialSensorReading: Float = -1f
    private var baseDailySteps: Int = 0
    private var currentDayDate: String = getTodayDateString()

    private var currentDailySteps: Int = 0
    private var currentSessionSteps: Int = 0

    // Walking session state
    var sessionState: WalkingSessionState = WalkingSessionState.IDLE
        private set

    // Accelerometer peak detection variables
    private var lastAccelMagnitude: Float = 9.8f
    private var lastStepTimestamp: Long = 0L
    private val stepThreshold = 2.2f // m/s^2 above gravity

    private var hasReceivedHardwareStepCounterEvent = false

    fun initDailySteps(persistedStepsToday: Int) {
        val today = getTodayDateString()
        if (today != currentDayDate) {
            currentDayDate = today
            baseDailySteps = 0
            currentDailySteps = 0
            initialSensorReading = -1f
            hasReceivedHardwareStepCounterEvent = false
        } else {
            baseDailySteps = persistedStepsToday
            currentDailySteps = persistedStepsToday
        }
        notifyMetrics()
    }

    fun startListening() {
        if (stepCounterSensor != null) {
            sensorManager?.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI)
        }
        if (stepDetectorSensor != null) {
            sensorManager?.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_UI)
        }
        if (accelerometerSensor != null) {
            sensorManager?.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopListening() {
        sensorManager?.unregisterListener(this)
    }

    fun startWalkingSession() {
        sessionState = WalkingSessionState.WALKING
        currentSessionSteps = 0
        startListening()
        notifyMetrics()
    }

    fun pauseWalkingSession() {
        sessionState = WalkingSessionState.PAUSED
        notifyMetrics()
    }

    fun resumeWalkingSession() {
        sessionState = WalkingSessionState.WALKING
        startListening()
        notifyMetrics()
    }

    fun finishWalkingSession() {
        sessionState = WalkingSessionState.IDLE
        currentSessionSteps = 0
        notifyMetrics()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        // Verify date rollover
        val today = getTodayDateString()
        if (today != currentDayDate) {
            currentDayDate = today
            baseDailySteps = 0
            currentDailySteps = 0
            initialSensorReading = event.values[0]
            hasReceivedHardwareStepCounterEvent = false
        }

        when (event.sensor.type) {
            Sensor.TYPE_STEP_COUNTER -> {
                hasReceivedHardwareStepCounterEvent = true
                val totalStepsSinceBoot = event.values[0]
                if (initialSensorReading < 0f) {
                    initialSensorReading = totalStepsSinceBoot
                }
                val deltaSteps = (totalStepsSinceBoot - initialSensorReading).toInt().coerceAtLeast(0)
                val newDailyTotal = baseDailySteps + deltaSteps
                val stepDifference = newDailyTotal - currentDailySteps
                if (stepDifference > 0) {
                    currentDailySteps = newDailyTotal
                    if (sessionState == WalkingSessionState.WALKING) {
                        currentSessionSteps += stepDifference
                    }
                    notifyMetrics()
                }
            }

            Sensor.TYPE_STEP_DETECTOR -> {
                hasReceivedHardwareStepCounterEvent = true
                if (event.values[0] == 1.0f) {
                    onSingleStepDetected()
                }
            }

            Sensor.TYPE_ACCELEROMETER -> {
                // Fallback physical motion detection for devices/emulators lacking dedicated pedometer
                if (!hasReceivedHardwareStepCounterEvent) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    val magnitude = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                    val delta = kotlin.math.abs(magnitude - 9.8f)

                    val now = System.currentTimeMillis()
                    if (delta > stepThreshold && (now - lastStepTimestamp) > 320) {
                        lastStepTimestamp = now
                        onSingleStepDetected()
                    }
                    lastAccelMagnitude = magnitude
                }
            }
        }
    }

    private fun onSingleStepDetected() {
        currentDailySteps++
        if (sessionState == WalkingSessionState.WALKING) {
            currentSessionSteps++
        }
        notifyMetrics()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op
    }

    /**
     * For developer/manual verification
     */
    fun addManualSteps(stepIncrement: Int) {
        currentDailySteps += stepIncrement
        if (sessionState == WalkingSessionState.WALKING) {
            currentSessionSteps += stepIncrement
        }
        notifyMetrics()
    }

    private fun notifyMetrics() {
        val dailyDistKm = (currentDailySteps * 0.72f) / 1000f
        val dailyCal = (currentDailySteps * 0.042f).toInt()
        val dailyActiveMin = (currentDailySteps / 105).coerceAtLeast(0)

        val sessionDistKm = (currentSessionSteps * 0.72f) / 1000f
        val sessionCal = (currentSessionSteps * 0.042f).toInt()

        onMetricsUpdated?.invoke(
            currentDailySteps,
            dailyDistKm,
            dailyCal,
            dailyActiveMin,
            currentSessionSteps,
            sessionDistKm,
            sessionCal
        )
    }

    companion object {
        fun getTodayDateString(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date())
        }
    }
}
