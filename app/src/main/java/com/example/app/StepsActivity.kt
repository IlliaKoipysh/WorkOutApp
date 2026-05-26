package com.example.app

class StepsActivity {
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