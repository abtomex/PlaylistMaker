package dom.dima.practicum.playlistmaker.domain.api

import dom.dima.practicum.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(searchStr: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }
}