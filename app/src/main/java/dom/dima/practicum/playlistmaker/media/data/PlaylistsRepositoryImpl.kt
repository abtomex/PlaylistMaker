package dom.dima.practicum.playlistmaker.media.data

import dom.dima.practicum.playlistmaker.media.data.converters.PlaylistDbConverter
import dom.dima.practicum.playlistmaker.media.data.db.AppDatabase
import dom.dima.practicum.playlistmaker.media.domain.PlaylistsRepository
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.media.domain.state.AddToDbState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    val appDatabase: AppDatabase,
    val playlistDbConverter: PlaylistDbConverter
) : PlaylistsRepository {

    override fun allPlaylists(): Flow<List<Playlist?>> {
        return appDatabase.playlistsDao().getAll()
            .map {it.map { playlistDbConverter.map(it) } }
    }

    override fun createPlaylist(playlist: Playlist) : Flow<AddToDbState<Playlist>> = flow {
        val found = appDatabase.playlistsDao().getOne(playlist.id)
        if (found != null) {
            appDatabase.playlistsDao().deleteById(found.id)
        }
        appDatabase.playlistsDao().saveOne(playlistDbConverter.map(playlist))
        emit(AddToDbState.Added(playlist))
    }

    override suspend fun getOnePlaylist(id: Int) : Playlist? {
        return playlistDbConverter.map(appDatabase.playlistsDao().getOne(id))
    }
}