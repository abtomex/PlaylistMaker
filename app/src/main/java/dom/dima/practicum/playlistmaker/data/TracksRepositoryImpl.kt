package dom.dima.practicum.playlistmaker.data

import dom.dima.practicum.playlistmaker.domain.models.Track
import dom.dima.practicum.playlistmaker.data.dto.TracksSearchRequest
import dom.dima.practicum.playlistmaker.data.dto.TracksSearchResponse
import dom.dima.practicum.playlistmaker.domain.api.ApiResponse
import dom.dima.practicum.playlistmaker.domain.api.TracksRepository
import java.util.Objects

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(searchStr: String): ApiResponse<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(searchStr))
        if (response.resultCode == 200) {
            return ApiResponse.Success((response as TracksSearchResponse).results.filter {
                Objects.nonNull(
                    it
                )
            }.map {
                Track(
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
                )
            })
        } else {
            return ApiResponse.Error("response result code is ${response.resultCode}")
        }
    }
}