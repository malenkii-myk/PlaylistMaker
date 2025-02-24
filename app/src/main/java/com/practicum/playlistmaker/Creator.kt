package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import com.practicum.playlistmaker.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.SharedPreferencesStorageClient
import com.practicum.playlistmaker.data.StorageClient
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.SettingsInteractor
import com.practicum.playlistmaker.domain.api.SettingsRepository
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    private lateinit var application: Application
    lateinit var storageClient: StorageClient

    fun initApplication(app: Application){
        this.application = app
        this.storageClient = SharedPreferencesStorageClient(application.applicationContext)
    }

    private fun getTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(storageClient)
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TracksInteractorImpl(getTrackRepository())
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

    private fun getSettingsRepository() : SettingsRepository {
        return SettingsRepositoryImpl(storageClient)
    }

    fun providerSettingsInteractor() : SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

}