package com.example.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DietActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_diet)
        bindTopBar(R.id.btnDietBack, R.id.btnDietEdit, R.id.btnDietSave)

        findViewById<Button>(R.id.btnAddMealCalories).setOnClickListener {
            val input = findViewById<EditText>(R.id.etMealCalories)
            val calories = input.text.toString().trim().toIntOrNull()
            if (calories == null || calories <= 0) {
                showTopMessage("Enter meal calories")
                return@setOnClickListener
            }
            AppCache.addMealCalories(this, calories)
            input.text.clear()
            updateDietValues()
            showTopMessage("Calories added")
        }

        updateDietValues()
    }

    private fun updateDietValues() {
        val settings = UserStorage.readSettings(this)
        val dailyGoal = settings.optInt("caloriesGoal", 2200)
        val consumed = AppCache.foodCaloriesForDate(this)
        val burned = AppCache.getInt(this, "caloriesBurnedToday", 0)
        val balance = StatsCalculator.dailyBalance(consumed, burned)
        val averageBalance = weeklyBalances().average().takeIf { !it.isNaN() }?.toInt() ?: 0

        findViewById<TextView>(R.id.tvTodayFoodConsumedValue).text = "Food Consumed    $consumed kcal"
        findViewById<TextView>(R.id.tvTodayCaloriesBurnedValue).text = "Calories Burned    $burned kcal"
        findViewById<TextView>(R.id.tvTodayDailyBalanceValue).text = "Daily Balance    $balance kcal"
        findViewById<TextView>(R.id.tvAverageDailyBalanceValue).text = "Average Daily Balance    $averageBalance kcal"

        val remaining = (dailyGoal - consumed + burned).coerceAtLeast(0)
        AppCache.putNumber(this, "caloriesRemainingToday", remaining)

        val labels = listOf(
            R.id.tvBalanceMon to "Mon",
            R.id.tvBalanceTue to "Tue",
            R.id.tvBalanceWed to "Wed",
            R.id.tvBalanceThu to "Thu",
            R.id.tvBalanceFri to "Fri",
            R.id.tvBalanceSat to "Sat",
            R.id.tvBalanceSun to "Sun"
        )
        weeklyBalances().forEachIndexed { index, value ->
            val (viewId, label) = labels[index]
            findViewById<TextView>(viewId).text = "$label\n$value"
        }
    }

    private fun weeklyBalances(): List<Int> {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        return (0..6).map {
            val key = formatter.format(calendar.time)
            val consumed = AppCache.foodCaloriesForDate(this, key)
            val burned = if (key == AppCache.todayKey()) AppCache.getInt(this, "caloriesBurnedToday", 0) else 0
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            StatsCalculator.dailyBalance(consumed, burned)
        }
    }
}
