package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(SP_DARK_THEME, false)
        switchTheme(darkTheme)

        super.onCreate()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
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

    private companion object {
        const val SHARED_PREFERENCES_NAME = "playlistmaker_preferences"
        const val SP_DARK_THEME = "dark_theme"
    }


}