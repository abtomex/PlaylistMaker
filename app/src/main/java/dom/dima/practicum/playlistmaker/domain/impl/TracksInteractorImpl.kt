package dom.dima.practicum.playlistmaker.domain.impl

import dom.dima.practicum.playlistmaker.domain.api.ApiResponse
import dom.dima.practicum.playlistmaker.domain.api.TracksInteractor
import dom.dima.practicum.playlistmaker.domain.api.TracksRepository
import dom.dima.practicum.playlistmaker.domain.consumer.ConsumerData
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

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
}