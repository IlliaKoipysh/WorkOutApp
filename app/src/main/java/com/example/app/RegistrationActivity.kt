package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)

        val username = findViewById<EditText>(R.id.inputUsername)
        val password = findViewById<EditText>(R.id.inputPassword)
        val confirmPassword = findViewById<EditText>(R.id.inputConfirmPassword)
        val goalGroup = findViewById<RadioGroup>(R.id.goalGroup)
        val btnCreate = findViewById<Button>(R.id.btnCreateAccount)

        btnCreate.setOnClickListener {

            val user = username.text.toString().trim()
            val pass = password.text.toString().trim()
            val confirm = confirmPassword.text.toString().trim()

            if (user.isEmpty()) {
                showTopMessage("Enter username")
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                showTopMessage("Enter password")
                return@setOnClickListener
            }

            if (pass != confirm) {
                showTopMessage("Passwords do not match")
                return@setOnClickListener
            }

            val selectedGoalId = goalGroup.checkedRadioButtonId
            if (selectedGoalId == -1) {
                showTopMessage("Select a goal")
                return@setOnClickListener
            }

            val selectedGoal = findViewById<RadioButton>(selectedGoalId).text.toString()
            UserStorage.saveUser(
                this,
                LocalUser(
                    username = user,
                    passwordHash = UserStorage.hashPassword(pass),
                    goal = selectedGoal,
                    isLoggedIn = true
                )
            )

            showTopMessage("Account created")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
