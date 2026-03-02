package dom.dima.practicum.playlistmaker.media.domain.impl

import android.net.Uri
import dom.dima.practicum.playlistmaker.media.domain.PlaylistsFilesRepository
import dom.dima.practicum.playlistmaker.media.domain.files.PlaylistsFilesInteractor
import kotlinx.coroutines.flow.Flow

class PlaylistsFilesInteractorImpl (
    private val playlistsFilesRepository : PlaylistsFilesRepository
): PlaylistsFilesInteractor {
    override suspend fun createPlaylistCover(uri: Uri): Flow<Uri> {
        return playlistsFilesRepository.createPlaylistCover(uri)
    }
}