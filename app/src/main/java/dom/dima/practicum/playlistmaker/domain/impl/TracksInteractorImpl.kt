package dom.dima.practicum.playlistmaker.domain.impl

import dom.dima.practicum.playlistmaker.domain.api.TracksInteractor
import dom.dima.practicum.playlistmaker.domain.api.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(searchStr: String, consumer: TracksInteractor.TracksConsumer) {

        executor.execute {
            consumer.consume(repository.searchTracks(searchStr))
        }
    }
}