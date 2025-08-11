package dom.dima.practicum.playlistmaker.data

import dom.dima.practicum.playlistmaker.domain.models.Track
import dom.dima.practicum.playlistmaker.data.dto.TracksSearchRequest
import dom.dima.practicum.playlistmaker.data.dto.TracksSearchResponse
import dom.dima.practicum.playlistmaker.domain.api.TracksRepository

class TracksRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {
    override fun searchTracks(searchStr: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(searchStr))
        if (response.resultCode == 200) {
            return (response as TracksSearchResponse).results.map { Track(
                 it.trackId,
                 it.trackName,
                 it.artistName,
                 it.trackTimeMillis,
                 it.artworkUrl100,
                 it.collectionName,
                 it.releaseDate,
                 it.primaryGenreName,
                 it.country,
                 it.previewUrl
            ) }
        } else {
            return emptyList()
        }
    }
}