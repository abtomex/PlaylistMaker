package dom.dima.practicum.playlistmaker.media.data

import dom.dima.practicum.playlistmaker.media.data.converters.PlaylistDbConverter
import dom.dima.practicum.playlistmaker.media.data.db.AppDatabase
import dom.dima.practicum.playlistmaker.media.domain.PlaylistsRepository
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    val appDatabase: AppDatabase,
    val playlistDbConverter: PlaylistDbConverter
) : PlaylistsRepository {

    override fun allPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistsDao().getAll()
            .map {it.map { playlistDbConverter.map(it) } }
    }

    override fun createPlaylist(playlist: Playlist) : Flow<Playlist> = flow {
        val found = appDatabase.playlistsDao().getOne(playlist.id)
        if (found != null) {
            appDatabase.playlistsDao().deleteById(found.id)
        }
        appDatabase.playlistsDao().saveOne(playlistDbConverter.map(playlist))
        emit(playlist)
    }

    override suspend fun getOnePlaylist(id: Int) : Playlist? {
        val foundEntity = appDatabase.playlistsDao().getOne(id) ?: return null
        return playlistDbConverter.map(foundEntity)
    }

    override fun save(playlist: Playlist) : Flow<Playlist> = flow {
        val playlistId = playlist.id
        appDatabase.playlistsDao().saveOne(playlistDbConverter.map(playlist))
        emit(playlistDbConverter.map(appDatabase.playlistsDao().getOne(playlistId)!!))
    }
}