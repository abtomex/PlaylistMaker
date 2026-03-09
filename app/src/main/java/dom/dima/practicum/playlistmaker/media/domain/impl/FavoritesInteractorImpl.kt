package dom.dima.practicum.playlistmaker.media.domain.impl

import dom.dima.practicum.playlistmaker.media.domain.TracksDbRepository
import dom.dima.practicum.playlistmaker.media.domain.db.FavoritesInteractor
import dom.dima.practicum.playlistmaker.media.domain.state.AddFavoriteState
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl (
    private val tracksDbRepository: TracksDbRepository
) : FavoritesInteractor {
    override fun favoriteTracks(): Flow<List<Track>> {
        return tracksDbRepository.favoriteTracks()
    }

    override suspend fun addToFavorites(track: Track): Flow<AddFavoriteState> {
        return tracksDbRepository.createFavorite(track)
    }

    override suspend fun favoriteStatus(track: Track): Flow<Boolean> {
        return tracksDbRepository.favoriteStatus(track)
    }

}