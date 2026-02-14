package dom.dima.practicum.playlistmaker.media.data

import dom.dima.practicum.playlistmaker.media.data.converters.TrackDbConverter
import dom.dima.practicum.playlistmaker.media.data.db.AppDatabase
import dom.dima.practicum.playlistmaker.media.domain.FavoritesRepository
import dom.dima.practicum.playlistmaker.media.domain.state.AddFavoriteState
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl (
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : FavoritesRepository {
    override fun favoriteTracks(): Flow<List<Track>> {
        return appDatabase.favoriteDao().getTracks()
            .map {it.map { trackDbConverter.map(it) }
        }
    }

    override fun createFavorite(track: Track): Flow<AddFavoriteState> = flow {
        val found = appDatabase.favoriteDao().getTrackById(track.trackId)
        if ( found != null ) {
            appDatabase.favoriteDao().deleteTrackById(track.trackId)
            emit(AddFavoriteState.Removed(trackDbConverter.map(found)))
        } else {
            appDatabase.favoriteDao().insertTrack(trackDbConverter.map(track))
            emit(AddFavoriteState.Added(track))
        }
    }

    override fun favoriteStatus(track: Track): Flow<Boolean> = flow {
        val found = appDatabase.favoriteDao().getTrackById(track.trackId)
        emit(found != null )
    }


}