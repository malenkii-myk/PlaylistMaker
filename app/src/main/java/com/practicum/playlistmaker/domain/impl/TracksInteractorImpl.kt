package com.practicum.playlistmaker.domain.impl

import android.util.Log
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.model.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun search(query: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            val trackList = repository.searchTracks(query)
            consumer.consume(trackList)
        }
    }

}