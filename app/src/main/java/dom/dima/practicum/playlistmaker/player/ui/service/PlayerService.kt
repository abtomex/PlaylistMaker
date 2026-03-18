package dom.dima.practicum.playlistmaker.player.ui.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import dom.dima.practicum.playlistmaker.player.ui.state.AudioPlayerState
import dom.dima.practicum.playlistmaker.player.ui.state.StateData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerService : Service() {

    private val mediaPlayer: MediaPlayer by inject()
    private val binder = PlayerServiceBinder()
    private val _playerState = MutableStateFlow<AudioPlayerState>(AudioPlayerState.Default(StateData(STATE_DEFAULT)))
    val playerState = _playerState.asStateFlow()
    private var trackUrl = ""

    private var timerJob: Job? = null


    override fun onBind(intent: Intent?): IBinder {
        trackUrl = intent?.getStringExtra("track_url") ?: ""
        preparePlayer(trackUrl)
        return binder
    }

    fun preparePlayer(url: String?) {
        if (url.isNullOrEmpty()) return
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            _playerState.value = AudioPlayerState.Prepared(StateData(STATE_PREPARED))
        }
        mediaPlayer.setOnCompletionListener {
            _playerState.value = AudioPlayerState.Completion(StateData(STATE_PREPARED))
        }

    }

    fun startPlayer() {
        mediaPlayer.start()
        _playerState.value =
            AudioPlayerState.Playing(
                StateData(STATE_PLAYING),
                getCurrentPlayerPosition()
            )
        startTimer()

    }

    fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        _playerState.value =
            AudioPlayerState.Pause(
                StateData(STATE_PAUSED)
            )
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            ?: "00:00"
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer.isPlaying) {
                delay(TIMER_DELAY_MS)
                _playerState.value = AudioPlayerState.Playing(
                    StateData(STATE_PLAYING),
                    getCurrentPlayerPosition()
                )

            }
        }

    }


    inner class PlayerServiceBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3

        const val TIMER_DELAY_MS = 300L

    }

}