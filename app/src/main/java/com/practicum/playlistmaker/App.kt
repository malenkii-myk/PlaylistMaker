package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.data.StorageClient
import com.practicum.playlistmaker.domain.api.SettingsInteractor

class App : Application() {

    private var darkTheme = false
    lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {

        super.onCreate()

        Creator.initApplication(this)
        settingsInteractor = Creator.providerSettingsInteractor()
        darkTheme = settingsInteractor.darkTheme
        settingsInteractor.switchTheme(darkTheme)
    }



    companion object {
        // SHARED_PREFERENCES
        const val SHARED_PREFERENCES_NAME = "playlistmaker_3a6ad9aa8a"
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