package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private var darkTheme = false
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(SP_DARK_THEME, false)
        switchTheme(darkTheme)

        super.onCreate()
    }

    fun isDarkTheme(): Boolean {
        return darkTheme
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        //val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(SP_DARK_THEME, darkThemeEnabled)
        editor.apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        // SHARED_PREFERENCES
        const val SHARED_PREFERENCES_NAME = "playlistmaker_preferences"
        const val SP_DARK_THEME = "dark_theme"
        const val SP_SEARCH_HISTORY = "search_history"
        const val KEY_INTENT_TRACK_DATA = "track_data"
        // SEARCH HISTORY ACTIVITY
        const val MAX_SEARCH_HISTORY = 10
        const val MIN_LENGTH_SEARCH_QUERY = 3
        const val SEARCH_DEBOUNCE_DELAY = 1000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
        // PLAYER ACTIVITY
        const val PLAYER_HANDLER_DELAY = 1000L
    }


}