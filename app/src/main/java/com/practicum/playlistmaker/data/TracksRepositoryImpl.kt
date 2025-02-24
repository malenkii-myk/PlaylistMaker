package com.practicum.playlistmaker.data

import android.util.Log
import com.practicum.playlistmaker.data.dto.TrackRequest
import com.practicum.playlistmaker.data.dto.TrackResponse
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.model.Resource
import com.practicum.playlistmaker.domain.model.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(query: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackRequest(query))
        if (response.resultCode == -1) {
            return Resource.Fail(emptyList())
        } else if (response.resultCode == 200) {
            return Resource.Success((response as TrackResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    Track.getFormattedTrackTime(it.trackTimeMillis),
                    it.artworkUrl100,
                    Track.getCoverArtwork(it.artworkUrl100),
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            })
        } else {
            return Resource.Success(emptyList())
        }
    }
}