package com.practicum.playlistmaker.data

import android.app.Application.MODE_PRIVATE
import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.App

class SharedPreferencesStorageClient(context: Context): StorageClient {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    override fun getBoolean(field: String): Boolean {
        return sharedPreferences.getBoolean(field, false)
    }

    override fun getString(field: String): String {
        return sharedPreferences.getString(field, null) ?: ""
    }

    override fun writeBoolean(field: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(field, value)
        editor.apply()
    }

    override fun writeString(field: String, value: String) {
        val editor = sharedPreferences.edit()
        if (field.isEmpty()) {
            editor.remove(field)
        } else {
            editor.putString(field, value)
        }
        editor.apply()
    }
}