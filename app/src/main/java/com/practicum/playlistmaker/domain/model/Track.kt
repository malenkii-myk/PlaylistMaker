package com.practicum.playlistmaker.domain.model

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackId: Long,     // ID
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val trackTimeFormatted: String,
    val artworkUrl100: String, // Ссылка на изображение обложки
    val artworkUrl: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
): Serializable {

    fun getFormattedTrackTime(): String {
        return getFormattedTrackTime(trackTimeMillis)
    }

    fun getCoverArtwork():String {
        return getCoverArtwork(artworkUrl100)
    }

    companion object{
        fun getFormattedTrackTime(trackTimeMillis: Long): String {
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
        }

        fun getCoverArtwork(artworkUrl100: String): String {
            return if (artworkUrl100.isNotEmpty()) {
                artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
            } else ""
        }
    }


}