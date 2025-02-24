package com.practicum.playlistmaker.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val trackBaseUrl = "https://itunes.apple.com"

    private val client: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(trackBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: trackApi by lazy {
        client.create(trackApi::class.java)
    }
}