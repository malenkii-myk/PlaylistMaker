package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.model.Resource
import com.practicum.playlistmaker.domain.model.Track

interface TrackInteractor {
    fun search(query: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(trackList: Resource<List<Track>>)
    }
}