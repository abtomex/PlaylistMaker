package dom.dima.practicum.playlistmaker.media.domain.impl

import dom.dima.practicum.playlistmaker.media.domain.PlaylistsRepository
import dom.dima.practicum.playlistmaker.media.domain.TracksDbRepository
import dom.dima.practicum.playlistmaker.media.domain.db.PlaylistsInteractor
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository,
    private val tracksDbRepository: TracksDbRepository
) : PlaylistsInteractor {

    override fun getAll(): Flow<List<Playlist>> {
        return playlistsRepository.allPlaylists()
    }

    override suspend fun getById(playlistId: Int): Flow<Playlist> = flow {
        val playlist = playlistsRepository.getPlaylistById(playlistId)!!
        val tracks = tracksDbRepository.getTracksById(playlist.trackIds)
        playlist.tracks.addAll(tracks)
        emit(playlist)
    }

    override suspend fun addOne(playlist: Playlist): Flow<Playlist> {
        return playlistsRepository.createPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist, track: Track) : Flow<Playlist> {
        val existsTrack = tracksDbRepository.getTrackById(track.trackId)
        if (existsTrack == null) {
            tracksDbRepository.createTrack(track)
        }
        playlist.trackIds.add(track.trackId)
        return playlistsRepository.save(playlist)
    }
}