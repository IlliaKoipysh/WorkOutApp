package com.example.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class ExercisesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercises)

        bindTopBar(R.id.btnExercisesBack, R.id.btnExercisesEdit, R.id.btnExercisesSave)

        findViewById<MaterialCardView>(R.id.btnBiceps).setOnClickListener {
            startActivity(Intent(this, BicepActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.btnTricepsChest).setOnClickListener {
            startActivity(Intent(this, TricepsChestActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.btnAbs).setOnClickListener {
            startActivity(Intent(this, AbsActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.btnLegs).setOnClickListener {
            startActivity(Intent(this, LegsActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.btnBack).setOnClickListener {
            startActivity(Intent(this, BackActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.btnLoseWeight).setOnClickListener {
            startActivity(Intent(this, LoseWeightActivity::class.java))
        }
    }
}
