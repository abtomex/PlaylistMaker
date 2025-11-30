package dom.dima.practicum.playlistmaker.presentation.state

import dom.dima.practicum.playlistmaker.domain.models.Track

sealed interface SearchState {

    object Loading : SearchState
    data class Error(val message: String) : SearchState
    data class Content(val data: List<Track>) : SearchState

}