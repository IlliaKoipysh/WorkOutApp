package com.example.app

import android.content.Context
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppCache {
    private const val FILE_NAME = "app_cache.json"

    fun readJson(context: Context): JSONObject {
        return runCatching {
            JSONObject(context.openFileInput(FILE_NAME).bufferedReader().use { it.readText() })
        }.getOrElse { JSONObject() }
    }

    fun saveJson(context: Context, json: JSONObject) {
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(json.toString().toByteArray())
        }
    }

    fun clear(context: Context) {
        context.deleteFile(FILE_NAME)
    }

    fun putNumber(context: Context, key: String, value: Number) {
        val json = readJson(context)
        json.put(key, value)
        saveJson(context, json)
    }

    fun getInt(context: Context, key: String, defaultValue: Int = 0): Int {
        return readJson(context).optInt(key, defaultValue)
    }

    fun getDouble(context: Context, key: String, defaultValue: Double = 0.0): Double {
        return readJson(context).optDouble(key, defaultValue)
    }

    fun getLong(context: Context, key: String, defaultValue: Long = 0L): Long {
        return readJson(context).optLong(key, defaultValue)
    }

    fun addMealCalories(context: Context, calories: Int, dateKey: String = todayKey()) {
        val json = readJson(context)
        val food = json.optJSONObject("food") ?: JSONObject()
        food.put(dateKey, food.optInt(dateKey, 0) + calories)
        json.put("food", food)
        saveJson(context, json)
    }

    fun foodCaloriesForDate(context: Context, dateKey: String = todayKey()): Int {
        return readJson(context).optJSONObject("food")?.optInt(dateKey, 0) ?: 0
    }

    fun todayKey(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }
}
