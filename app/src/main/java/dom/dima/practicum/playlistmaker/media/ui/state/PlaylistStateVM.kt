package dom.dima.practicum.playlistmaker.media.ui.state

import android.net.Uri
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist

sealed class PlaylistStateVM {
    class Added(val playlist: Playlist): PlaylistStateVM()
    class Error(val errorMessage: String): PlaylistStateVM()
    class CoverCreated(val uri: Uri): PlaylistStateVM()
}