package com.practicum.playlistmaker.domain.model

sealed interface Resource<T> {
    data class Success<T>(val data: T): Resource<T>
    data class Fail<T>(val data: T): Resource<T>
}