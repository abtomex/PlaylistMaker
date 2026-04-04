package dom.dima.practicum.playlistmaker.player.ui.view_model

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.media.domain.db.FavoritesInteractor
import dom.dima.practicum.playlistmaker.media.domain.db.PlaylistsInteractor
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.media.domain.state.AddFavoriteState
import dom.dima.practicum.playlistmaker.player.ui.service.PlayerService
import dom.dima.practicum.playlistmaker.player.ui.service.PlayerServiceImpl
import dom.dima.practicum.playlistmaker.player.ui.state.AudioPlayerState
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val gson: Gson,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
    private val context: Context

) : ViewModel() {

    private val playerState = MutableLiveData<AudioPlayerState>()
    fun getPlayerState(): LiveData<AudioPlayerState> = playerState
    private var musicService: PlayerService? = null

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerServiceImpl.PlayerServiceBinder
            musicService = binder.getService()

            viewModelScope.launch {
                musicService?.getPlayerState()?.collect {
                    playerState.postValue(it)
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
        }
    }

    fun unbindAudioPlayer() {
        try {
            context.unbindService(serviceConnection)
        } catch (_: IllegalArgumentException) {
            // Сервис уже отвязан
        }
    }

    fun bindAudioPlayer(previewUrl: String?) {
        val intent = Intent(context, PlayerServiceImpl::class.java).apply {
            putExtra("track_url", previewUrl)
        }

        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }


    fun fromJson(trackJson: String?, javaClass: Class<Track>): Track {
        return gson.fromJson(trackJson, javaClass)
    }


    fun addToFavoriteOrRemove(track: Track) {
        viewModelScope.launch {
            favoritesInteractor.addToFavorites(track).collect { state ->
                when (state) {
                    is AddFavoriteState.Added -> playerState.postValue(AudioPlayerState.Favorite())
                    is AddFavoriteState.Removed -> playerState.postValue(AudioPlayerState.NotFavorite())
                }
            }
        }

    }

    fun initButtonLikeStatus(track: Track) {
        viewModelScope.launch {
            favoritesInteractor.favoriteStatus(track).collect { isInFavorites ->
                when (isInFavorites) {
                    true -> playerState.postValue(AudioPlayerState.Favorite())
                    false -> playerState.postValue(AudioPlayerState.NotFavorite())
                }
            }
        }
    }

    fun addTrackToPlaylist(playlist: Playlist, track: Track) {

        if (playlist.trackIds.contains(track.trackId)) {
            playerState.postValue(AudioPlayerState.AlreadyExists(playlist.title))
            return
        }

        viewModelScope.launch {
            playlistsInteractor
                .updatePlaylist(playlist, track)
                .collect {
                    playerState.postValue(AudioPlayerState.CompleteAddToPlaylist(playlist.title))
                }
        }


    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistsInteractor.getAll().collect {
                playerState.postValue(AudioPlayerState.Playlists(it))
            }
        }
    }

    fun pausePlayer() {
        musicService?.pausePlayer()
    }

    fun startPlayer() {
        musicService?.startPlayer()
    }

    fun foregroundNotification(track: Track) {
        val appName = context.getString(R.string.app_name)
        musicService?.foregroundNotification(appName, track)
    }

    fun removeForegroundNotification() {
        musicService?.removeForegroundNotification()
    }
    fun stopService() {
        val intent = Intent(context, PlayerServiceImpl::class.java)
        context.stopService(intent)
        musicService = null
    }

}