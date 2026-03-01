package dom.dima.practicum.playlistmaker.media.domain.impl

import dom.dima.practicum.playlistmaker.media.domain.PlaylistsRepository
import dom.dima.practicum.playlistmaker.media.domain.TracksDbRepository
import dom.dima.practicum.playlistmaker.media.domain.db.PlaylistsInteractor
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.stream.Collectors

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository,
    private val tracksDbRepository: TracksDbRepository
) : PlaylistsInteractor {

    override fun getAll(): Flow<List<Playlist>> = flow {
        emit(playlistsRepository.allPlaylists())
    }

    override suspend fun getById(playlistId: Int): Flow<Playlist> = flow {
        val playlist = playlistsRepository.getPlaylistById(playlistId)!!
        val tracks = tracksDbRepository.getTracksById(playlist.trackIds)
        playlist.tracks.addAll(tracks)
        emit(playlist)
    }

    override suspend fun addOne(playlist: Playlist): Flow<Unit> = flow {
        emit(playlistsRepository.createPlaylist(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist, track: Track): Flow<Playlist> {
        val existsTrack = tracksDbRepository.getTrackById(track.trackId)
        if (existsTrack == null) {
            tracksDbRepository.createTrack(track)
        }
        playlist.trackIds.add(track.trackId)
        return playlistsRepository.save(playlist)
    }

    override suspend fun removeTrackFromPlaylist(
        track: Track,
        playlist: Playlist
    ): Flow<Playlist> = flow {

        playlist.trackIds.remove(track.trackId)
        playlistsRepository.updateTracksIds(playlist.trackIds, playlist.id)

        val updatedPlaylist = playlistsRepository.getPlaylistById(playlist.id)
        updatedPlaylist?.tracks?.addAll(tracksDbRepository.getTracksById(playlist.trackIds))
        checkAndRemoveOrphanTracks()
        emit(updatedPlaylist!!)
    }

    private suspend fun checkAndRemoveOrphanTracks() {

        val tracksInPlaylists = playlistsRepository.allPlaylists().stream().flatMap { it.trackIds.stream() }.collect(Collectors.toSet())

        val candidatesForRemove = tracksDbRepository.getAll().stream()
            .filter { !it.isFavorite }
            .filter { !tracksInPlaylists.contains(it.trackId) }
            .map { it.trackId }
            .collect(Collectors.toSet())

        candidatesForRemove.forEach {
            tracksDbRepository.deleteTrackById(it)
        }
    }
}