package com.example.app

import android.content.Intent
import android.widget.ImageButton
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.bindTopBar(
    @IdRes backId: Int,
    @IdRes editId: Int,
    @IdRes saveId: Int
) {
    findViewById<ImageButton>(backId).setOnClickListener { finish() }
    findViewById<ImageButton>(editId).setOnClickListener {
        if (this is SettingsActivity) {
            showTopMessage("Already in settings")
        } else {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
    findViewById<ImageButton>(saveId).setOnClickListener {
        showTopMessage("Saved")
    }
}
