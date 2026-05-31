package com.example.app

import android.content.Context
import org.json.JSONObject
import java.security.MessageDigest

data class LocalUser(
    val username: String,
    val passwordHash: String,
    val goal: String,
    val isLoggedIn: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)

object UserStorage {
    private const val FILE_NAME = "user_data.json"

    fun saveUser(context: Context, user: LocalUser) {
        val existingSettings = readSettings(context)
        val json = JSONObject()
            .put("username", user.username)
            .put("passwordHash", user.passwordHash)
            .put("goal", user.goal)
            .put("isLoggedIn", user.isLoggedIn)
            .put("createdAt", user.createdAt)
            .put("settings", existingSettings)

        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(json.toString().toByteArray())
        }
    }

    fun readUser(context: Context): LocalUser? {
        return runCatching {
            val json = JSONObject(context.openFileInput(FILE_NAME).bufferedReader().use { it.readText() })
            LocalUser(
                username = json.optString("username"),
                passwordHash = json.optString("passwordHash"),
                goal = json.optString("goal"),
                isLoggedIn = json.optBoolean("isLoggedIn", false),
                createdAt = json.optLong("createdAt", System.currentTimeMillis())
            )
        }.getOrNull()
    }

    fun isLoggedIn(context: Context): Boolean = readUser(context)?.isLoggedIn == true

    fun readJson(context: Context): JSONObject {
        return runCatching {
            JSONObject(context.openFileInput(FILE_NAME).bufferedReader().use { it.readText() })
        }.getOrElse { JSONObject() }
    }

    fun saveJson(context: Context, json: JSONObject) {
        if (!json.has("createdAt")) {
            json.put("createdAt", System.currentTimeMillis())
        }
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(json.toString().toByteArray())
        }
    }

    fun readSettings(context: Context): JSONObject {
        return readJson(context).optJSONObject("settings") ?: JSONObject()
    }

    fun updateSettings(context: Context, settings: JSONObject) {
        val json = readJson(context)
        if (!json.has("createdAt")) {
            json.put("createdAt", System.currentTimeMillis())
        }
        json.put("settings", settings)
        saveJson(context, json)
    }

    fun clear(context: Context) {
        context.deleteFile(FILE_NAME)
    }

    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
