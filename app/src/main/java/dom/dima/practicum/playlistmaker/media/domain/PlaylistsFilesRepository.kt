package dom.dima.practicum.playlistmaker.media.domain

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface PlaylistsFilesRepository {
    suspend fun createPlaylistCover(uri: Uri): Flow<Uri>
}