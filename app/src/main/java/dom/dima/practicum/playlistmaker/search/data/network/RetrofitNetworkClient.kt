package dom.dima.practicum.playlistmaker.search.data.network

import dom.dima.practicum.playlistmaker.search.data.dto.Response
import dom.dima.practicum.playlistmaker.search.data.dto.TracksSearchRequest

class RetrofitNetworkClient(private val trackApiService: SearchTrackApiService): NetworkClient {

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