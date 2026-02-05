package dom.dima.practicum.playlistmaker.search.domain

import dom.dima.practicum.playlistmaker.settings.domain.consumer.LoadingData
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(searchStr: String): Flow<LoadingData<List<Track>>>
    fun returnHistoryTracks(): Flow<LoadingData<List<Track>>>
    fun addToHistory(track: Track)
    fun clearHistory()

}