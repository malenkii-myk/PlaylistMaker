package com.practicum.playlistmaker.domain.api

interface SettingsInteractor {
    var darkTheme: Boolean
    fun isDarkTheme(): Boolean
    fun switchTheme(darkThemeEnabled: Boolean)
}