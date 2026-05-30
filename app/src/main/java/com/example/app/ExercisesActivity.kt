package com.example.app

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.card.MaterialCardView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ExercisesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercises)

        findViewById<ImageButton>(R.id.btnExercisesBack).setOnClickListener {
            finish()
        }

        findViewById<MaterialCardView>(R.id.btnBiceps).setOnClickListener {
            Toast.makeText(this, "Biceps", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialCardView>(R.id.btnTricepsChest).setOnClickListener {
            Toast.makeText(this, "Triceps & Chest", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialCardView>(R.id.btnAbs).setOnClickListener {
            Toast.makeText(this, "Abs", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialCardView>(R.id.btnLegs).setOnClickListener {
            Toast.makeText(this, "Legs", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialCardView>(R.id.btnBack).setOnClickListener {
            Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialCardView>(R.id.btnLoseWeight).setOnClickListener {
            Toast.makeText(this, "Lose Weight", Toast.LENGTH_SHORT).show()
        }
    }
}