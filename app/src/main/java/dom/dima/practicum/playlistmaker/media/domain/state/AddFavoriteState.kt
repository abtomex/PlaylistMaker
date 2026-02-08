package dom.dima.practicum.playlistmaker.media.domain.state

import dom.dima.practicum.playlistmaker.search.domain.models.Track

sealed class AddFavoriteState(track: Track) {
    class Added(track: Track): AddFavoriteState(track)
    class Removed(track: Track): AddFavoriteState(track)
}