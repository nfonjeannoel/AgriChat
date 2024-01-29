package com.heavyair.agrichat

import android.content.Context

class PreferencesHelper(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("MyApp", Context.MODE_PRIVATE)

    fun saveSessionId(sessionId: String) {
        sharedPreferences.edit().putString("SESSION_ID", sessionId).apply()
    }

    fun getSessionId(): String? {
        return sharedPreferences.getString("SESSION_ID", null)
    }
}