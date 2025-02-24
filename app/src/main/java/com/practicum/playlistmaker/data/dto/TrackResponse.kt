package com.practicum.playlistmaker.data.dto

class TrackResponse (
    // val resultCount: Int,
    val results: List<TrackDto> = emptyList()
): Response()