package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private val gson = Gson()

    fun addTrack(track: Track) {
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

    fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(App.SP_SEARCH_HISTORY, null) ?: return emptyList()
        val type = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearHistory() {
        sharedPreferences.edit().remove(App.SP_SEARCH_HISTORY).apply()
    }

    private fun saveHistory(history: List<Track>) {
        val json = gson.toJson(history)
        sharedPreferences.edit().putString(App.SP_SEARCH_HISTORY, json).apply()
    }

}