package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        findViewById<LinearLayout?>(R.id.cardSteps)?.setOnClickListener {
            startActivity(Intent(this, StepsActivity::class.java))
        }

        findViewById<LinearLayout?>(R.id.cardProgress)?.setOnClickListener {
            startActivity(Intent(this, ProgressActivity::class.java))
        }

        findViewById<LinearLayout?>(R.id.cardCardio)?.setOnClickListener {
            startActivity(Intent(this, CardioActivity::class.java))
        }

        findViewById<LinearLayout?>(R.id.cardExercises)?.setOnClickListener {
            startActivity(Intent(this, ExercisesActivity::class.java))
        }

        findViewById<LinearLayout?>(R.id.cardDiet)?.setOnClickListener {
            startActivity(Intent(this, DietActivity::class.java))
        }

        findViewById<LinearLayout?>(R.id.cardSettings)?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        findViewById<Button?>(R.id.btnSaveProgress)?.setOnClickListener {
            Toast.makeText(this, "Progress saved", Toast.LENGTH_SHORT).show()
        }
    }
}