package dom.dima.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dom.dima.practicum.playlistmaker.search.domain.TracksInteractor
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.search.ui.state.SearchState
import dom.dima.practicum.playlistmaker.settings.domain.consumer.ConsumerData

class SearchViewModel(
    private val tracksInteractor: TracksInteractor
) : ViewModel () {


    private val state = MutableLiveData<SearchState>()
    fun getState(): LiveData<SearchState> = state

    fun loadData(searchTrack: String) {
        state.value = SearchState.Loading

        tracksInteractor.searchTracks(
            searchStr = searchTrack,
            consumer = object : TracksInteractor.TracksConsumer {

                override fun consume(data: ConsumerData<List<Track>>) {

                    when(data) {
                        is ConsumerData.Data -> {
                            val content = SearchState.Content(data.value)
                            state.postValue(content)
                        }
                        is ConsumerData.Error -> {
                            val error = SearchState.Error(data.message)
                            state.postValue(error)
                        }
                    }

                }

            }
        )

    }

}