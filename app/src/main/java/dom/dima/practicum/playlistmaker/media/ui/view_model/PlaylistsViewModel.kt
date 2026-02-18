package dom.dima.practicum.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dom.dima.practicum.playlistmaker.media.domain.db.PlaylistsInteractor
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {
    private val playlistsState = MutableLiveData<List<Playlist>>()
    fun getPlaylistsState(): LiveData<List<Playlist>> = playlistsState

    fun initPlaylistsList() {
        viewModelScope.launch {
            playlistsInteractor.getAll().collect {
                playlistsState.postValue(it)
            }
        }
    }

}