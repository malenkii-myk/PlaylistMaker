package com.practicum.playlistmaker

import androidx.annotation.Nullable
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Track(
    val trackId: Long,     // ID
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String? = null, // Ссылка на изображение обложки
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
): Serializable {

    fun getFormattedTrackTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    }

    fun getCoverArtwork():String {
        val artworkUrl = artworkUrl100 ?: ""
        return artworkUrl.replaceAfterLast('/', "512x512bb.jpg")
    }


}