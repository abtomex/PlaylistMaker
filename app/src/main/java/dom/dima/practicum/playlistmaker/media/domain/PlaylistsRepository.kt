package dom.dima.practicum.playlistmaker.media.domain

import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun allPlaylists() : Flow<List<Playlist>>
    fun createPlaylist(playlist: Playlist) : Flow<Playlist>
    suspend fun getOnePlaylist(id: Int) : Playlist?
}