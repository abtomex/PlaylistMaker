package dom.dima.practicum.playlistmaker.media.domain.db

import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun getAll(): Flow<List<Playlist>>
    suspend fun getById(playlistId: Int): Flow<Playlist>
    suspend fun addOne(playlist: Playlist): Flow<Unit>
    suspend fun updatePlaylist(playlist: Playlist, track: Track) : Flow<Playlist>
    suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist) : Flow<Playlist>
}