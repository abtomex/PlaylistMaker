package dom.dima.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dom.dima.practicum.playlistmaker.search.domain.TracksInteractor
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.search.ui.state.SearchState

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val gson: Gson
) : ViewModel () {


    var performedSearchStr: String = ""
    private val state = MutableLiveData<SearchState>()
    fun getState(): LiveData<SearchState> = state

    fun loadData(searchTrack: String) {

        state.value = SearchState.Loading
        tracksInteractor.searchTracks(
            searchStr = searchTrack,
            consumer = TracksConsumer(state)
        )

    }

    fun gson(): Gson {
        return gson
    }

    fun loadHistoryTracks() {

        state.value = SearchState.Loading
        tracksInteractor.returnHistoryTracks(TracksConsumer(state))

    }

    fun addToHistory(track: Track) {
        tracksInteractor.addToHistory(track)

    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
    }


}