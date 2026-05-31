package com.example.app

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.util.concurrent.TimeUnit

class ProgressActivity : AppCompatActivity() {
    private lateinit var progressChart: BarChart
    private var selectedMetric = "Steps"
    private var selectedRange = "Week"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_progress)

        bindTopBar(R.id.btnBack, R.id.btnEdit, R.id.btnSave)
        progressChart = findViewById(R.id.progressChart)
        bindFilters()
        updateProgress()
        updateChart()
    }

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

    private fun updateProgress() {
        val now = System.currentTimeMillis()
        val createdAt = UserStorage.readUser(this)?.createdAt ?: now
        val lastVisit = AppCache.getLong(this, "lastProgressVisitAt", now)
        val completedDays = TimeUnit.MILLISECONDS.toDays(now - createdAt).toInt().coerceAtLeast(0)
        val daysSinceLastVisit = TimeUnit.MILLISECONDS.toDays(now - lastVisit).toInt().coerceAtLeast(0)

        findViewById<TextView>(R.id.tvCompletedWorkoutDaysValue).text = completedDays.toString()
        findViewById<TextView>(R.id.tvLastWorkoutDaysValue).text = daysSinceLastVisit.toString()
        AppCache.putNumber(this, "lastProgressVisitAt", now)

        val settings = UserStorage.readSettings(this)
        findViewById<TextView>(R.id.tvProgressTargetStepsValue).text =
            "${settings.optInt("stepsGoal", 3000)}\nSteps"
        findViewById<TextView>(R.id.tvProgressTargetDistanceValue).text =
            "%.1f\nkm".format(settings.optDouble("distanceGoal", 2.0))
        findViewById<TextView>(R.id.tvProgressTargetCaloriesValue).text =
            "${settings.optInt("caloriesGoal", 300)}\nCalories"
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
            BarEntry(index.toFloat(), if (index == points - 1) latest else 0f)
        }
        val dataSet = BarDataSet(entries, "$selectedMetric / $selectedRange")
        dataSet.color = 0xFFF3A047.toInt()
        progressChart.data = BarData(dataSet)
        progressChart.description.isEnabled = false
        progressChart.invalidate()
    }
}
