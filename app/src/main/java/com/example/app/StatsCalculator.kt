package com.example.app

object StatsCalculator {
    fun distanceKmFromSteps(steps: Int): Double = steps * 0.75 / 1000.0

    fun caloriesFromDistance(distanceKm: Double, weightKg: Double): Double {
        return distanceKm * weightKg
    }

    fun dailyBalance(consumedCalories: Int, burnedCalories: Int): Int {
        return consumedCalories - burnedCalories
    }
}
