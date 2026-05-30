package com.example.app

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ProgressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_progress)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.tvCompletedWorkoutDaysValue).text = "5"
        findViewById<TextView>(R.id.tvLastWorkoutDaysValue).text = "2"

        findViewById<TextView>(R.id.tvProgressTargetStepsValue).text =
            "8000\nSteps"

        findViewById<TextView>(R.id.tvProgressTargetDistanceValue).text =
            "10\nkm"

        findViewById<TextView>(R.id.tvProgressTargetCaloriesValue).text =
            "3000\nCalories"
    }
}