package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.model.Track

interface SearchHistoryRepository {
    fun addTrack(track: Track)
    fun getHistory(): List<Track>
    fun clearHistory()
    // fun saveHistory(history: List<Track>)
}