package com.practicum.playlistmaker.data

import android.content.Context

interface StorageClient {
    fun getBoolean(field: String): Boolean
    fun getString(field: String): String
    fun writeBoolean(field: String, value: Boolean)
    fun writeString(field: String, value: String)
}