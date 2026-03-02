package dom.dima.practicum.playlistmaker.media.domain.db

import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun getAll(): Flow<List<Playlist>>
    suspend fun addOne(playlist: Playlist): Flow<Playlist>
    suspend fun updatePlaylist(playlist: Playlist) : Flow<Playlist>
}