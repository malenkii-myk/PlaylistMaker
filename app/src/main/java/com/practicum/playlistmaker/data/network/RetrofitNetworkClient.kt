

package com.practicum.playlistmaker.data.network
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.TrackRequest
import com.practicum.playlistmaker.data.dto.TrackResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    override fun doRequest(dto: Any): TrackResponse {
        return try {
            if (dto is TrackRequest) {
                val response = RetrofitClient.api.search(dto.query).execute()
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