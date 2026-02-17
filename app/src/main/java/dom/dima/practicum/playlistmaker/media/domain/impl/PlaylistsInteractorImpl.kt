package dom.dima.practicum.playlistmaker.media.domain.impl

import dom.dima.practicum.playlistmaker.media.domain.PlaylistsRepository
import dom.dima.practicum.playlistmaker.media.domain.db.PlaylistsInteractor
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    val playlistsRepository: PlaylistsRepository
) : PlaylistsInteractor {

    override fun getAll(): Flow<List<Playlist>> {
        return playlistsRepository.allPlaylists()
    }

    override suspend fun addOne(playlist: Playlist): Flow<Playlist> {
        return playlistsRepository.createPlaylist(playlist)
    }
}