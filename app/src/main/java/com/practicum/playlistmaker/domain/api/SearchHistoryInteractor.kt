package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.model.Track

interface SearchHistoryInteractor {
    fun addTrack(track: Track)
    fun getHistory(): List<Track>
    fun clearHistory()
}