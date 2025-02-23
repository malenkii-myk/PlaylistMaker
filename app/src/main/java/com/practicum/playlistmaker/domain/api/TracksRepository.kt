package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.model.Resource
import com.practicum.playlistmaker.domain.model.Track

interface TracksRepository {
    fun searchTracks(query: String): Resource<List<Track>>
}