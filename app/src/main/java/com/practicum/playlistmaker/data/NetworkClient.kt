package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackResponse

interface NetworkClient {
    fun doRequest(dto: Any): TrackResponse
}