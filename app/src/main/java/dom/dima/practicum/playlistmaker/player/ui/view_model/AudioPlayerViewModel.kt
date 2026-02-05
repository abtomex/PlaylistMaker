package dom.dima.practicum.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dom.dima.practicum.playlistmaker.player.ui.state.AudioPlayerState
import dom.dima.practicum.playlistmaker.player.ui.state.StateData
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val gson: Gson,
    private val mediaPlayer: MediaPlayer
) : ViewModel() {

    private val state = MutableLiveData<AudioPlayerState>()
    fun getState(): LiveData<AudioPlayerState> = state

    private var timerJob: Job? = null

    fun fromJson(trackJson: String?, javaClass: Class<Track>): Track {
        return gson.fromJson(trackJson, javaClass)
    }

    fun preparePlayer(url: String?) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            state.postValue(AudioPlayerState.Prepared(StateData(STATE_PREPARED)))
        }
        mediaPlayer.setOnCompletionListener {
            state.postValue(AudioPlayerState.Completion(StateData(STATE_PREPARED)))
        }

    }

    fun startPlayer() {
        mediaPlayer.start()
        state.postValue(AudioPlayerState.Playing(StateData(STATE_PLAYING), getCurrentPlayerPosition()))
        startTimer()

    }

    fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        state.postValue(AudioPlayerState.Pause(StateData(STATE_PAUSED), getCurrentPlayerPosition()))
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition) ?: "00:00"
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(TIMER_DELAY_MS)
                state.postValue(AudioPlayerState.Playing(StateData(STATE_PLAYING), getCurrentPlayerPosition()))
            }
        }
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3

        const val TIMER_DELAY_MS = 300L

    }


}