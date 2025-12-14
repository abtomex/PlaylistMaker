package dom.dima.practicum.playlistmaker.search.domain

import dom.dima.practicum.playlistmaker.settings.domain.consumer.ConsumerData
import dom.dima.practicum.playlistmaker.search.domain.models.Track

interface TracksInteractor {
    fun searchTracks(searchStr: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(data: ConsumerData<List<Track>>)
    }
}