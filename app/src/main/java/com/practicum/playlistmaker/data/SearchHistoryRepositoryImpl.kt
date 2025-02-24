package com.practicum.playlistmaker.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.model.Track


class SearchHistoryRepositoryImpl(private val storageClient: StorageClient):
    SearchHistoryRepository {

    private val gson = Gson()

    override fun addTrack(track: Track) {
        val historyList = getHistory().toMutableList()
        if (historyList.contains(track)) {
            historyList.remove(track)
        }
        historyList.add(0, track)
        if (historyList.size > App.MAX_SEARCH_HISTORY) {
            historyList.removeAt(historyList.size - 1)
        }
        saveHistory(historyList)
    }

    override fun getHistory(): List<Track> {
        val json = storageClient.getString(App.SP_SEARCH_HISTORY)
        if (json.isBlank()) {
            return emptyList()
        }
        val type = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(json, type)
    }

    override fun clearHistory() {
        storageClient.writeString(App.SP_SEARCH_HISTORY, "")
    }

    private fun saveHistory(history: List<Track>) {
        val json = gson.toJson(history)
        storageClient.writeString(App.SP_SEARCH_HISTORY, json)
    }

}