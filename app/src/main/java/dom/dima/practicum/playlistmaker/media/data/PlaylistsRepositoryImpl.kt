package dom.dima.practicum.playlistmaker.media.data

import androidx.core.net.toFile
import dom.dima.practicum.playlistmaker.media.data.converters.PlaylistDbConverter
import dom.dima.practicum.playlistmaker.media.data.db.AppDatabase
import dom.dima.practicum.playlistmaker.media.domain.PlaylistsRepository
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl (
    val appDatabase: AppDatabase,
    val playlistDbConverter: PlaylistDbConverter
) : PlaylistsRepository {

    override suspend fun allPlaylists(): List<Playlist> {
         return appDatabase.playlistsDao().getAll()
            .map {
                playlistDbConverter.map(it)
            }
    }

    override suspend fun createPlaylist (playlist: Playlist) {
        appDatabase.playlistsDao().saveOne(playlistDbConverter.map(playlist))
    }

    override suspend fun getPlaylistById(id: Int) : Playlist? {
        val foundEntity = appDatabase.playlistsDao().getOne(id) ?: return null
        return playlistDbConverter.map(foundEntity)
    }

    override fun save(playlist: Playlist) : Flow<Playlist> = flow {
        val playlistId = playlist.id
        appDatabase.playlistsDao().saveOne(playlistDbConverter.map(playlist))
        emit(playlistDbConverter.map(appDatabase.playlistsDao().getOne(playlistId)!!))
    }

    override suspend fun updateTracksIds (
        trackIds: MutableSet<Int>,
        playlistId: Int
    ) {
        appDatabase.playlistsDao().updateTrackIds(playlistDbConverter.mapTrackIds(trackIds), playlistId)
    }

    override suspend fun delete(playlist: Playlist) {
        appDatabase.playlistsDao().deleteById(playlist.id)
    }

    override suspend fun updateInfo(playlist: Playlist?) {
        if (playlist == null) return
        appDatabase.playlistsDao().updatePlaylistInfo(playlist.id, playlist.title, playlist.cover?.toFile()?.name, playlist.description)
    }

}