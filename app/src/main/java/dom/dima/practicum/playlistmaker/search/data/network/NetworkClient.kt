package dom.dima.practicum.playlistmaker.search.data.network

import dom.dima.practicum.playlistmaker.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}