package com.example.app

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class CardioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cardio)

        findViewById<ImageButton>(R.id.btnCardioBack).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.tvAvgHeartRateValue).text = "128 bpm"
        findViewById<TextView>(R.id.tvTotalTimeValue).text = "184 min"
        findViewById<TextView>(R.id.tvTotalCaloriesValue).text = "1430 kcal"

        findViewById<TextView>(R.id.tvSummaryDistanceValue).text = "7.4 km\nDistance"
        findViewById<TextView>(R.id.tvSummaryStepsValue).text = "9832\nSteps"
        findViewById<TextView>(R.id.tvSummaryDurationValue).text = "52 min\nDuration"
        findViewById<TextView>(R.id.tvSummaryCaloriesValue).text = "487 kcal\nCalories"
    }
}