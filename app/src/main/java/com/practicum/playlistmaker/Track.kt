package com.practicum.playlistmaker

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
){
    companion object {
        val IMG_HOST: String = "https://is5-ssl.mzstatic.com/image/thumb/"
    }
}