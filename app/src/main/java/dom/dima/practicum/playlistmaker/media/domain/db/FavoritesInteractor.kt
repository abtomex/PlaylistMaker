package dom.dima.practicum.playlistmaker.media.domain.db

import dom.dima.practicum.playlistmaker.media.domain.state.AddFavoriteState
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {

    fun favoriteTracks(): Flow<List<Track>>
    suspend fun addToFavorites(track: Track): Flow<AddFavoriteState>
    fun favoriteStatus(track: Track): Boolean
}