package com.example.app

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class CardioActivity : AppCompatActivity(), SensorEventListener {
    private var sensorManager: SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cardio)

        bindTopBar(R.id.btnCardioBack, R.id.btnCardioEdit, R.id.btnCardioSave)
        SensorDataHelper.requestLocation(this)
        SensorDataHelper.requestActivityRecognition(this)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        updateCardioValues()
    }

    override fun onResume() {
        super.onResume()
        val heartRateSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        if (heartRateSensor != null) {
            sensorManager?.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        sensorManager?.unregisterListener(this)
        super.onPause()
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
            val heartRate = event.values.firstOrNull()?.toInt() ?: return
            if (heartRate > 0) {
                AppCache.putNumber(this, "avgHeartRate", heartRate)
                updateCardioValues()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    private fun updateCardioValues() {
        val steps = AppCache.getInt(this, "stepsToday", 0)
        val distanceKm = AppCache.getDouble(this, "distanceTodayKm", StatsCalculator.distanceKmFromSteps(steps))
        val burnedCalories = AppCache.getInt(this, "caloriesBurnedToday", 0)
        val durationMinutes = AppCache.getInt(this, "cardioDurationMinutes", 0)
        val avgHeartRate = AppCache.getInt(this, "avgHeartRate", 0)

        findViewById<TextView>(R.id.tvAvgHeartRateValue).text =
            if (avgHeartRate > 0) "$avgHeartRate bpm" else "N/A"
        findViewById<TextView>(R.id.tvTotalTimeValue).text = "$durationMinutes min"
        findViewById<TextView>(R.id.tvTotalCaloriesValue).text = "$burnedCalories kcal"

        findViewById<TextView>(R.id.tvSummaryDistanceValue).text = "%.1f km\nDistance".format(distanceKm)
        findViewById<TextView>(R.id.tvSummaryStepsValue).text = "$steps\nSteps"
        findViewById<TextView>(R.id.tvSummaryDurationValue).text = "$durationMinutes min\nDuration"
        findViewById<TextView>(R.id.tvSummaryCaloriesValue).text = "$burnedCalories kcal\nCalories"
    }
}
