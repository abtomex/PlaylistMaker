package dom.dima.practicum.playlistmaker.search.domain.impl

import dom.dima.practicum.playlistmaker.search.domain.TracksInteractor
import dom.dima.practicum.playlistmaker.search.domain.TracksRepository
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.settings.domain.api.ApiResponse
import dom.dima.practicum.playlistmaker.settings.domain.consumer.ConsumerData
import java.util.concurrent.Executor

class TracksInteractorImpl(private val repository: TracksRepository, private val executor: Executor ) : TracksInteractor {

    override fun searchTracks(searchStr: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {

            when (val searchResponse = repository.searchTracks(searchStr)) {
                is ApiResponse.Success -> {
                    consumer.consume(ConsumerData.Data(searchResponse.data))
                }
                is ApiResponse.Error -> {
                    consumer.consume(ConsumerData.Error("Что-то пошло не так ${searchResponse.message}"))
                }
            }

        }
    }

    override fun returnHistoryTracks(consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            val history = repository.historyTracks()
            consumer.consume(ConsumerData.HistoryData(history))
        }
    }

    override fun addToHistory(track: Track) {
        repository.addToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}