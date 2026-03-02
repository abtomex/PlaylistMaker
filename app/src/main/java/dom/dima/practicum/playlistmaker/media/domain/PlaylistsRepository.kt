package dom.dima.practicum.playlistmaker.media.domain

import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun allPlaylists() : List<Playlist>
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun getPlaylistById(id: Int) : Playlist?
    fun save(playlist: Playlist) : Flow<Playlist>
    suspend fun updateTracksIds(trackIds: MutableSet<Int>, playlistId: Int)
    suspend fun delete(playlist: Playlist)
    suspend fun updateInfo(playlist: Playlist?)
}