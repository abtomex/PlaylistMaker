package dom.dima.practicum.playlistmaker.media.domain

import dom.dima.practicum.playlistmaker.media.domain.state.AddFavoriteState
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun favoriteTracks(): Flow<List<Track>>
    fun createFavorite(track: Track): Flow<AddFavoriteState>
    fun favoriteStatus(track: Track): Flow<Boolean>

}