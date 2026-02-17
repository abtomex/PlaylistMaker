package dom.dima.practicum.playlistmaker.media.domain.files

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface PlaylistsFilesInteractor {
    suspend fun createPlaylistCover(uri: Uri) : Flow<String>
}