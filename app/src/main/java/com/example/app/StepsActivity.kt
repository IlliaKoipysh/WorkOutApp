package com.example.app

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlin.math.roundToInt

class StepsActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var stepsChart: BarChart
    private var sensorManager: SensorManager? = null
    private var selectedMetric = "Steps"
    private var selectedRange = "Week"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps)

        bindTopBar(R.id.btnBack, R.id.btnEdit, R.id.btnSave)
        SensorDataHelper.requestActivityRecognition(this)

        stepsChart = findViewById(R.id.stepsChart)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        bindFilters()
        updateStats()
        updateChart()
    }

    override fun onResume() {
        super.onResume()
        val stepCounter = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepCounter != null) {
            sensorManager?.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        sensorManager?.unregisterListener(this)
        super.onPause()
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val steps = event.values.firstOrNull()?.toInt() ?: return
            AppCache.putNumber(this, "stepsToday", steps)
            updateStats()
            updateChart()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    private fun bindFilters() {
        mapOf(
            R.id.btnFilterSteps to "Steps",
            R.id.btnFilterDistance to "Distance",
            R.id.btnFilterCalories to "Calories"
        ).forEach { (id, metric) ->
            findViewById<TextView>(id).setOnClickListener {
                selectedMetric = metric
                updateChart()
            }
        }

        mapOf(
            R.id.btnRangeWeek to "Week",
            R.id.btnRangeMonth to "Month",
            R.id.btnRangeYear to "Year"
        ).forEach { (id, range) ->
            findViewById<TextView>(id).setOnClickListener {
                selectedRange = range
                updateChart()
            }
        }
    }

    private fun updateStats() {
        val settings = UserStorage.readSettings(this)
        val weightKg = settings.optDouble("weight", 70.0)
        val steps = SensorDataHelper.readApproxStepCount(this)
        val distanceKm = StatsCalculator.distanceKmFromSteps(steps)
        val calories = StatsCalculator.caloriesFromDistance(distanceKm, weightKg).roundToInt()

        AppCache.putNumber(this, "stepsToday", steps)
        AppCache.putNumber(this, "distanceTodayKm", distanceKm)
        AppCache.putNumber(this, "caloriesBurnedToday", calories)

        findViewById<TextView>(R.id.tvStepsValue).text = steps.toString()
        findViewById<TextView>(R.id.tvDistanceValue).text = "%.1f km".format(distanceKm)
        findViewById<TextView>(R.id.tvCaloriesValue).text = "$calories kcal"

        findViewById<TextView>(R.id.tvTargetStepsValue).text =
            settings.optInt("stepsGoal", 3000).toString()
        findViewById<TextView>(R.id.tvTargetDistanceValue).text =
            "%.1f".format(settings.optDouble("distanceGoal", 2.0))
        findViewById<TextView>(R.id.tvTargetCaloriesValue).text =
            settings.optInt("caloriesGoal", 300).toString()
    }

    private fun updateChart() {
        val points = when (selectedRange) {
            "Month" -> 30
            "Year" -> 12
            else -> 7
        }
        val latest = when (selectedMetric) {
            "Distance" -> AppCache.getDouble(this, "distanceTodayKm", 0.0).toFloat()
            "Calories" -> AppCache.getInt(this, "caloriesBurnedToday", 0).toFloat()
            else -> AppCache.getInt(this, "stepsToday", 0).toFloat()
        }
        val entries = (0 until points).map { index ->
            val scale = if (index == points - 1) 1f else 0f
            BarEntry(index.toFloat(), latest * scale)
        }
        val dataSet = BarDataSet(entries, "$selectedMetric / $selectedRange")
        dataSet.color = 0xFFF3A047.toInt()
        stepsChart.data = BarData(dataSet)
        stepsChart.description.isEnabled = false
        stepsChart.invalidate()
    }
}
