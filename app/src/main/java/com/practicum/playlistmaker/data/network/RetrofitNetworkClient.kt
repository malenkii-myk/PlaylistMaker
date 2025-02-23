

package com.practicum.playlistmaker.data.network
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.TrackRequest
import com.practicum.playlistmaker.data.dto.TrackResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    private val trackBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(trackApi::class.java)

    override fun doRequest(dto: Any): TrackResponse {
        return try {
            if (dto is TrackRequest) {
                val response = trackService.search(dto.query).execute()
                val body = response.body() ?: TrackResponse()
                body.apply { resultCode = response.code() }
            } else {
                TrackResponse().apply { resultCode = 400 }
            }
        } catch (ex: Exception) {
            TrackResponse().apply { resultCode = -1 }
        }
    }


}