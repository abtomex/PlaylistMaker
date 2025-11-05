package dom.dima.practicum.playlistmaker.data

import dom.dima.practicum.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}