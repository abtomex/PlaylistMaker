package dom.dima.practicum.playlistmaker.search.ui.state

import dom.dima.practicum.playlistmaker.search.domain.models.Track

sealed interface SearchState {

    data object Loading : SearchState
    data class Error(val message: String) : SearchState
    data class Content(val data: List<Track>) : SearchState

    data class History(val data: List<Track>) : SearchState

}