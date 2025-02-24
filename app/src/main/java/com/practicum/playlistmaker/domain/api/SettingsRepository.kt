package com.practicum.playlistmaker.domain.api

interface SettingsRepository {
    var darkTheme: Boolean
    fun isDarkTheme(): Boolean
    fun switchTheme(darkThemeEnabled: Boolean)
}