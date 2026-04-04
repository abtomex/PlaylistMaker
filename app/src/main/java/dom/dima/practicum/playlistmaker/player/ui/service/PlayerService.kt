package dom.dima.practicum.playlistmaker.player.ui.service

import dom.dima.practicum.playlistmaker.player.ui.state.AudioPlayerState
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.StateFlow

interface PlayerService {
    fun pausePlayer()
    fun startPlayer()
    suspend fun getPlayerState(): StateFlow<AudioPlayerState>
    fun foregroundNotification(header: String, track: Track)
    fun removeForegroundNotification()
}