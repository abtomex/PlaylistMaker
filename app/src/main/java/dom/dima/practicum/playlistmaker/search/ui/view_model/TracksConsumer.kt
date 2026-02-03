package dom.dima.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dom.dima.practicum.playlistmaker.search.domain.TracksInteractor
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.search.ui.state.SearchState
import dom.dima.practicum.playlistmaker.settings.domain.consumer.ConsumerData

class TracksConsumer(val state: MutableLiveData<SearchState>) : TracksInteractor.TracksConsumer {
    override fun consume(data: ConsumerData<List<Track>>) {

        when(data) {
            is ConsumerData.Data -> {
                val content = SearchState.Content(data.value)
                state.postValue(content)
            }
            is ConsumerData.HistoryData -> {
                val content = SearchState.History(data.value)
                state.postValue(content)
            }
            is ConsumerData.Error -> {
                val error = SearchState.Error(data.message)
                state.postValue(error)
            }
        }

    }

}