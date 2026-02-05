package dom.dima.practicum.playlistmaker.search.domain

import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.settings.domain.api.ApiResponse

interface TracksRepository {
    suspend fun searchTracks(searchStr: String): ApiResponse<List<Track>>
    suspend fun historyTracks(): ApiResponse<List<Track>>
    fun addToHistory(track: Track)
    fun clearHistory()
}