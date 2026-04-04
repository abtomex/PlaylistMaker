package dom.dima.practicum.playlistmaker.player.ui.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.player.ui.state.AudioPlayerState
import dom.dima.practicum.playlistmaker.player.ui.state.StateData
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerServiceImpl : Service(), PlayerService {

    private val mediaPlayer: MediaPlayer by inject()
    private val binder = PlayerServiceBinder()
    private val _playerState =
        MutableStateFlow<AudioPlayerState>(AudioPlayerState.Default(StateData(STATE_DEFAULT)))

    override suspend fun getPlayerState(): StateFlow<AudioPlayerState> = _playerState.asStateFlow()
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
    }

    override fun startPlayer() {
        mediaPlayer.start()
        _playerState.value = AudioPlayerState.Playing(
            StateData(STATE_PLAYING),
            getCurrentPlayerPosition()
        )
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        stopTimer()
        _playerState.value = AudioPlayerState.Pause(
            StateData(STATE_PAUSED)
        )
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }


    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            ?: "00:00"
    }

    private fun startTimer() {
        stopTimer()
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer.isPlaying) {
                delay(TIMER_DELAY_MS)
                _playerState.value = AudioPlayerState.Playing(
                    StateData(STATE_PLAYING),
                    getCurrentPlayerPosition()
                )
            }
            _playerState.value = AudioPlayerState.Completion(StateData(STATE_PREPARED))
            removeForegroundNotification()

        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Music service",
            NotificationManager.IMPORTANCE_LOW
        )
        channel.description = "Service for playing music"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun createNotification(header: String, track: Track): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(header)
            .setContentText("${track.artistName}-${track.trackName}")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    override fun foregroundNotification(header: String, track: Track) {
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createNotification(header, track),
            getForegroundServiceTypeConstant()
        )
    }

    override fun removeForegroundNotification() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    inner class PlayerServiceBinder : Binder() {
        fun getService(): PlayerServiceImpl = this@PlayerServiceImpl

    }
    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val TIMER_DELAY_MS = 300L
        const val NOTIFICATION_CHANNEL_ID = "playlist_maker_channel"
        const val SERVICE_NOTIFICATION_ID = 100
    }

}