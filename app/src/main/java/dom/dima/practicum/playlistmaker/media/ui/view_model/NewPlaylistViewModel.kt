package dom.dima.practicum.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dom.dima.practicum.playlistmaker.media.domain.db.PlaylistsInteractor
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val newPlaylistState = MutableLiveData<List<Playlist>>()
    fun getNewPlaylistState(): LiveData<List<Playlist>> = newPlaylistState
    fun createPlaylist(title: String?, description: String?) {
        viewModelScope.launch {
            playlistsInteractor
                .addOne(Playlist(0, title, description, null, null, null))
                .collect {
                    println("+++++++++++++++++++++++")
                    println("new playlist was added")
                    println("----------------------")


                }
        }
    }


}
