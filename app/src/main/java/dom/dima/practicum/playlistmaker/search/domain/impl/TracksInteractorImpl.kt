package dom.dima.practicum.playlistmaker.search.domain.impl

import dom.dima.practicum.playlistmaker.search.domain.TracksInteractor
import dom.dima.practicum.playlistmaker.search.domain.TracksRepository
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.settings.domain.api.ApiResponse
import dom.dima.practicum.playlistmaker.settings.domain.consumer.LoadingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    override fun searchTracks(searchStr: String) = flow {
        emit(repository.searchTracks(searchStr))
    }
        .flowOn(Dispatchers.IO)
        .map { searchResponse ->
            when (searchResponse) {
                is ApiResponse.Success -> {
                    return@map LoadingData.Data(searchResponse.data)
                }

                is ApiResponse.Error -> {
                    return@map LoadingData.Error("Что-то пошло не так ${searchResponse.message}")
                }
                is ApiResponse.NoInternet -> {
                    return@map LoadingData.NoInternet("Проверьте подключение к интернету")
                }
            }
        }


    override fun returnHistoryTracks() = flow {
        emit(repository.historyTracks())
    }
        .flowOn(Dispatchers.IO)
        .map { searchResponse ->
            return@map LoadingData.HistoryData((searchResponse as ApiResponse.Success).data)
        }


    override fun addToHistory(track: Track) {
        repository.addToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}