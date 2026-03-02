package dom.dima.practicum.playlistmaker.media.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dom.dima.practicum.playlistmaker.media.domain.db.PlaylistsInteractor
import dom.dima.practicum.playlistmaker.media.domain.files.PlaylistsFilesInteractor
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.media.ui.state.PlaylistStateVM
import kotlinx.coroutines.launch

class PlaylistEditorViewModel (
    private val playlistsInteractor: PlaylistsInteractor,
    private val playlistsFilesInteractor: PlaylistsFilesInteractor
) : ViewModel() {

    private val playlistState = MutableLiveData<PlaylistStateVM>()
    fun getPlaylistState(): LiveData<PlaylistStateVM> = playlistState
    fun createPlaylist(title: String, coverUri: Uri?, description: String?) {
        viewModelScope.launch {
            playlistsInteractor
                .addOne(Playlist(title = title, description = description, cover =  coverUri))
                .collect {
                    playlistState.postValue(PlaylistStateVM.Added())
                }
        }
    }

    fun updatePlaylist(playlistId: Int, title: String, coverUri: Uri?, description: String?) {
        viewModelScope.launch {
            playlistsInteractor.updatePlaylistInfo(playlistId, title, coverUri, description)
                .collect {
                    playlistState.postValue(PlaylistStateVM.Updated())
                }
        }
    }

    fun saveImageToPrivateStorage(uri: Uri) {

        viewModelScope.launch {
            playlistsFilesInteractor.createPlaylistCover(uri)
                .collect {
                    playlistState.postValue(PlaylistStateVM.CoverCreated(it))
                }
        }

    }

    fun loadData(playlistId: Int) {
        viewModelScope.launch {
            playlistsInteractor.getById(playlistId).collect {
                playlistState.postValue(PlaylistStateVM.LoadData(it))
            }
        }
    }

}
