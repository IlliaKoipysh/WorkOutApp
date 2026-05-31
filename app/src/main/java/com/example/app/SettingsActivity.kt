package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import org.json.JSONObject

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<ImageButton>(R.id.btnEdit).setOnClickListener { showTopMessage("Already in settings") }
        findViewById<ImageButton>(R.id.btnSave).setOnClickListener { saveSettings() }

        bindSettingsActions()
        loadSettings()
    }

    private fun bindSettingsActions() {
        findViewById<TextView>(R.id.tvMainGoalValue).setOnClickListener {
            chooseTextValue(
                title = "Main Goal",
                options = arrayOf("Build Muscle", "General Fitness", "Lose Weight"),
                target = R.id.tvMainGoalValue
            )
        }

        findViewById<TextView>(R.id.btnStepCounterPermission).setOnClickListener {
            SensorDataHelper.requestActivityRecognition(this)
            showTopMessage("Step counter permission requested")
        }

        findViewById<TextView>(R.id.btnDistanceTrackingPermission).setOnClickListener {
            SensorDataHelper.requestLocation(this)
            showTopMessage("GPS permission requested")
        }

        findViewById<TextView>(R.id.btnThemeSelector).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Choose Theme")
                .setItems(arrayOf("Default Gym", "Custom Image")) { _, which ->
                    val value = if (which == 0) "Default Gym" else "Custom Image"
                    findViewById<TextView>(R.id.tvSelectedTheme).text = value
                    if (value == "Custom Image") {
                        SensorDataHelper.requestGallery(this)
                    }
                }
                .show()
        }

        findViewById<TextView>(R.id.btnUnitsSelector).setOnClickListener {
            chooseTextValue(
                title = "Units",
                options = arrayOf("kg / km / kcal", "lb / mi / kcal"),
                target = R.id.tvSelectedUnits
            )
        }

        findViewById<TextView>(R.id.btnClearSavedData).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Clear Saved Data")
                .setMessage("This removes saved profile, settings, and cached activity data.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Clear") { _, _ ->
                    UserStorage.clear(this)
                    AppCache.clear(this)
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                .show()
        }
    }

    private fun chooseTextValue(title: String, options: Array<String>, target: Int) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(options) { _, which ->
                findViewById<TextView>(target).text = options[which]
            }
            .show()
    }

    private fun loadSettings() {
        val user = UserStorage.readUser(this)
        val settings = UserStorage.readSettings(this)

        setText(R.id.etUserName, settings.optString("name", user?.username.orEmpty()))
        setText(R.id.etUserAge, settings.optString("age", ""))
        setText(R.id.etUserWeight, settings.optString("weight", ""))
        setText(R.id.etUserHeight, settings.optString("height", ""))
        findViewById<TextView>(R.id.tvMainGoalValue).text =
            settings.optString("mainGoal", user?.goal ?: "General Fitness")

        setText(R.id.etStepsGoal, settings.optString("stepsGoal", "3000"))
        setText(R.id.etCaloriesGoal, settings.optString("caloriesGoal", "300"))
        setText(R.id.etWorkoutTimeGoal, settings.optString("workoutTimeGoal", "30"))
        setText(R.id.etDistanceGoal, settings.optString("distanceGoal", "2"))

        findViewById<TextView>(R.id.tvSelectedTheme).text =
            settings.optString("theme", "Default Gym")
        findViewById<TextView>(R.id.tvSelectedUnits).text =
            settings.optString("units", "kg / km / kcal")
        findViewById<SwitchCompat>(R.id.switchWorkoutReminders).isChecked =
            settings.optBoolean("workoutReminders", false)
        findViewById<SwitchCompat>(R.id.switchHydrationReminder).isChecked =
            settings.optBoolean("hydrationReminder", false)
    }

    private fun saveSettings() {
        val settings = JSONObject()
            .put("name", editText(R.id.etUserName))
            .put("age", editText(R.id.etUserAge).toIntOrNull() ?: 0)
            .put("weight", editText(R.id.etUserWeight).toDoubleOrNull() ?: 70.0)
            .put("height", editText(R.id.etUserHeight).toIntOrNull() ?: 0)
            .put("mainGoal", findViewById<TextView>(R.id.tvMainGoalValue).text.toString())
            .put("stepsGoal", editText(R.id.etStepsGoal).toIntOrNull() ?: 3000)
            .put("caloriesGoal", editText(R.id.etCaloriesGoal).toIntOrNull() ?: 300)
            .put("workoutTimeGoal", editText(R.id.etWorkoutTimeGoal).toIntOrNull() ?: 30)
            .put("distanceGoal", editText(R.id.etDistanceGoal).toDoubleOrNull() ?: 2.0)
            .put("theme", findViewById<TextView>(R.id.tvSelectedTheme).text.toString())
            .put("units", findViewById<TextView>(R.id.tvSelectedUnits).text.toString())
            .put("workoutReminders", findViewById<SwitchCompat>(R.id.switchWorkoutReminders).isChecked)
            .put("hydrationReminder", findViewById<SwitchCompat>(R.id.switchHydrationReminder).isChecked)

        val json = UserStorage.readJson(this)
        val username = editText(R.id.etUserName)
        if (username.isNotBlank()) {
            json.put("username", username)
        }
        json.put("goal", settings.optString("mainGoal"))
        json.put("settings", settings)
        UserStorage.saveJson(this, json)
        showTopMessage("Settings saved")
    }

    private fun setText(id: Int, value: String) {
        findViewById<EditText>(id).setText(value)
    }

    private fun editText(id: Int): String {
        return findViewById<EditText>(id).text.toString().trim()
    }
}
