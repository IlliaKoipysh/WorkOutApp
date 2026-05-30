package com.example.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class StepsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps)

        val stepsChart = findViewById<BarChart>(R.id.stepsChart)

        val entries = listOf(
            BarEntry(0f, 4200f),
            BarEntry(1f, 6500f),
            BarEntry(2f, 8000f),
            BarEntry(3f, 5100f),
            BarEntry(4f, 7200f),
            BarEntry(5f, 9000f),
            BarEntry(6f, 6234f)
        )

        val dataSet = BarDataSet(entries, "Steps")
        val barData = BarData(dataSet)

        stepsChart.data = barData
        stepsChart.invalidate()
    }
}
