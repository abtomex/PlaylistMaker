package dom.dima.practicum.playlistmaker.media.domain

import dom.dima.practicum.playlistmaker.media.domain.state.AddFavoriteState
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksDbRepository {
    fun favoriteTracks(): Flow<List<Track>>
    fun createFavorite(track: Track): Flow<AddFavoriteState>
    fun favoriteStatus(track: Track): Flow<Boolean>
    suspend fun getTracksById(ids: Set<Int>): Set<Track>
    suspend fun createTrack(track: Track)
    suspend fun getTrackById(trackId: Int): Track?
    suspend fun getAll() : List<Track>
    suspend fun deleteTrackById(trackId: Int)

}