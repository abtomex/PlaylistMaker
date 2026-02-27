package dom.dima.practicum.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dom.dima.practicum.playlistmaker.media.domain.db.PlaylistsInteractor
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(
    val playlistsInteractor: PlaylistsInteractor,
    val gson: Gson
) : ViewModel() {

    private val playlistState = MutableLiveData<Playlist>()
    fun getPlaylistState(): LiveData<Playlist> = playlistState

    fun loadPlaylistData(playlistId: Int) {
        viewModelScope.launch {
            playlistsInteractor.getById(playlistId)
                .collect { playlistState.postValue(it) }

        }


    }

    fun toJson(track: Track): String {
        return gson.toJson(track)
    }

}