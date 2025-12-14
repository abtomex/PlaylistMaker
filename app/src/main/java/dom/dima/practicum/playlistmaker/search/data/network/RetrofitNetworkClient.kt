package dom.dima.practicum.playlistmaker.search.data.network

import dom.dima.practicum.playlistmaker.search.data.dto.Response
import dom.dima.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    private val imdbBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackApiService = retrofit.create(SearchTrackApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            val resp = trackApiService.search(dto.searchStr).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}