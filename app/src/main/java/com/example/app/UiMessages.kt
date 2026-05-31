package com.example.app

import android.app.Activity
import android.view.Gravity
import android.widget.Toast

fun Activity.showTopMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 96)
        show()
    }
}
