package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.data.StorageClient

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {

        super.onCreate()

        Creator.initApplication(this)
        darkTheme = Creator.storageClient.getBoolean(SP_DARK_THEME)
        switchTheme(darkTheme)
    }

    fun isDarkTheme(): Boolean {
        return darkTheme
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        Creator.storageClient.writeBoolean(SP_DARK_THEME, darkThemeEnabled)
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