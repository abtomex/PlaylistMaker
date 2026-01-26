package dom.dima.practicum.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dom.dima.practicum.playlistmaker.player.ui.state.AudioPlayerState
import dom.dima.practicum.playlistmaker.player.ui.state.StateData
import dom.dima.practicum.playlistmaker.search.domain.models.Track

class AudioPlayerViewModel(
    private val gson: Gson,
    private val mediaPlayer: MediaPlayer
) : ViewModel() {

    private val state = MutableLiveData<AudioPlayerState>()
    fun getState(): LiveData<AudioPlayerState> = state

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

    fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun startPlayer() {
        mediaPlayer.start()
        state.postValue(AudioPlayerState.Start(StateData(STATE_PLAYING)))
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        state.postValue(AudioPlayerState.Pause(StateData(STATE_PAUSED)))
    }

    fun releasePlayer() {
        mediaPlayer.release()
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3

    }


}