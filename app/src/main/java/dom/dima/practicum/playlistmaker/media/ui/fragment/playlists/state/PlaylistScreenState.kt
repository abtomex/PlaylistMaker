package dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.state

import dom.dima.practicum.playlistmaker.media.domain.models.Playlist

sealed class PlaylistScreenState(val playlist: Playlist) {
    class LoadData(val data: Playlist) : PlaylistScreenState(data)
    class ReloadData(val data: Playlist) : PlaylistScreenState (data)
}