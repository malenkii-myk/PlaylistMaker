package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.SettingsInteractor
import com.practicum.playlistmaker.domain.api.SettingsRepository

class SettingsInteractorImpl (private val repository: SettingsRepository) : SettingsInteractor {

    override var darkTheme: Boolean
        get() = repository.darkTheme
        set(value) {
            repository.switchTheme(value)
        }

    override fun isDarkTheme(): Boolean {
        return repository.isDarkTheme()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        repository.switchTheme(darkThemeEnabled)
    }

}