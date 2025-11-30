package dom.dima.practicum.playlistmaker.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dom.dima.practicum.playlistmaker.creator.Creator
import dom.dima.practicum.playlistmaker.domain.api.TracksInteractor
import dom.dima.practicum.playlistmaker.domain.models.Track
import dom.dima.practicum.playlistmaker.domain.consumer.ConsumerData
import dom.dima.practicum.playlistmaker.presentation.state.SearchState

class SearchViewModel(): ViewModel () {

    private val tracksInteractor = Creator.provideTracksInteractor()

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