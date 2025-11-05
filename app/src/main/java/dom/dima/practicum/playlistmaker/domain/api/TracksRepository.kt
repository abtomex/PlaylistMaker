package dom.dima.practicum.playlistmaker.domain.api

import dom.dima.practicum.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(searchStr: String): List<Track>
}