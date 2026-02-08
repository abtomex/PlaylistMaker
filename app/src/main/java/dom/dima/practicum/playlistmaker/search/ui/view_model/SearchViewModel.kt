package dom.dima.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dom.dima.practicum.playlistmaker.search.domain.TracksInteractor
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.search.ui.state.SearchState
import dom.dima.practicum.playlistmaker.settings.domain.consumer.LoadingData
import dom.dima.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val gson: Gson
) : ViewModel() {

    @Volatile
    private var searchInProgress = false

    var clickIsAllowed: Boolean = true

    private var latestSearchText: String? = null
    var performedSearchStr: String = ""

    private var searchJob: Job? = null

    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            loadData(changedText)
        }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    private val state = MutableLiveData<SearchState>()
    fun getState(): LiveData<SearchState> = state

    fun loadData(searchTrack: String) {

        state.value = SearchState.Loading

        viewModelScope.launch {
            tracksInteractor.searchTracks(
                searchStr = searchTrack
            ).collect { data -> processData(data) }

        }
    }

    private fun processData(data : LoadingData<List<Track>>) {
        searchInProgress = false
        when (data) {
            is LoadingData.Data -> {
                val content = SearchState.Content(data.value)
                state.postValue(content)
            }

            is LoadingData.HistoryData -> {
                val content = SearchState.History(data.value)
                state.postValue(content)
            }

            is LoadingData.Error -> {
                val error = SearchState.Error(data.message)
                state.postValue(error)
            }

        }

    }
    fun gson(): Gson {
        return gson
    }

    fun loadHistoryTracks() {

        searchJob?.cancel()
        searchJob = null

        state.value = SearchState.Loading
        viewModelScope.launch {
            tracksInteractor.returnHistoryTracks()
                .collect { data -> processData(data) }
        }

    }

    fun addToHistory(track: Track) {
        tracksInteractor.addToHistory(track)

    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
    }

    fun clickDebounce() {
        viewModelScope.launch {
            delay(CLICK_DEBOUNCE_DELAY)
            clickIsAllowed = true
        }

    }

    fun scheduleSearch(searchTrack : String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            doSearch(searchTrack)
        }
    }

    fun doSearch(searchTrack : String) {

        performedSearchStr = searchTrack
        searchInProgress = true

        searchDebounce(searchTrack)
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}