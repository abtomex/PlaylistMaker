package dom.dima.practicum.playlistmaker.ui.search.state

import dom.dima.practicum.playlistmaker.domain.models.Track

sealed interface SearchState {

    data object Loading : SearchState
    data class Error(val message: String) : SearchState
    data class Content(val data: List<Track>) : SearchState

}