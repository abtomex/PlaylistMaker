package dom.dima.practicum.playlistmaker.media.ui.state

import android.net.Uri

sealed class PlaylistStateVM {
    class Added(): PlaylistStateVM()
    class Error(val errorMessage: String): PlaylistStateVM()
    class CoverCreated(val uri: Uri): PlaylistStateVM()
}