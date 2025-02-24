package com.practicum.playlistmaker.data

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.App.Companion.SP_DARK_THEME
import com.practicum.playlistmaker.domain.api.SettingsRepository

class SettingsRepositoryImpl(storageClient: StorageClient) : SettingsRepository {

    private val storageClient: StorageClient = storageClient

    override var darkTheme: Boolean
        get() = storageClient.getBoolean(App.SP_DARK_THEME)
        set(value) {
            switchTheme(value)
        }

    override fun isDarkTheme(): Boolean {
        return darkTheme
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        storageClient.writeBoolean(SP_DARK_THEME, darkThemeEnabled)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}