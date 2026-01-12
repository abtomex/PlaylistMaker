package dom.dima.practicum.playlistmaker.search.domain

import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.settings.domain.api.ApiResponse

interface TracksRepository {
    fun searchTracks(searchStr: String): ApiResponse<List<Track>>
}