package dom.dima.practicum.playlistmaker.media.data

import dom.dima.practicum.playlistmaker.media.data.converters.TrackDbConverter
import dom.dima.practicum.playlistmaker.media.data.db.AppDatabase
import dom.dima.practicum.playlistmaker.media.domain.TracksDbRepository
import dom.dima.practicum.playlistmaker.media.domain.state.AddFavoriteState
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.stream.Collectors

class TracksDbRepositoryImpl (
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : TracksDbRepository {
    override fun favoriteTracks(): Flow<List<Track>> {
        return appDatabase.tracksDao().getFavoriteTracks()
            .map {it.map { trackDbConverter.map(it) }
        }
    }

    override fun createFavorite(track: Track): Flow<AddFavoriteState> = flow {
        val found = appDatabase.tracksDao().getTrackById(track.trackId)
        if ( found != null && found.isFavorite) {
            track.isFavorite = false
            appDatabase.tracksDao().insertTrack(trackDbConverter.map(track))
            emit(AddFavoriteState.Removed(trackDbConverter.map(found)))
        } else {
            track.isFavorite = true
            appDatabase.tracksDao().insertTrack(trackDbConverter.map(track))
            emit(AddFavoriteState.Added(track))
        }
    }

    override fun favoriteStatus(track: Track): Flow<Boolean> = flow {
        val found = appDatabase.tracksDao().getTrackById(track.trackId)
        emit(found?.isFavorite ?: false)
    }

    override suspend fun getTracksById(ids: Set<Int>): Set<Track> {
        return appDatabase.tracksDao().getTracksByIds(ids).stream().map { trackDbConverter.map(it) }.collect(Collectors.toSet())
    }

    override suspend fun createTrack(track: Track) {
        appDatabase.tracksDao().insertTrack(trackDbConverter.map(track))
    }

    override suspend fun getTrackById(trackId: Int) : Track? {
        return trackDbConverter.map(appDatabase.tracksDao().getTrackById(trackId))
    }


}