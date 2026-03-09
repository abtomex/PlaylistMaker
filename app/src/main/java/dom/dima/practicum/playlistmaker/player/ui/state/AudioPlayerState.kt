package dom.dima.practicum.playlistmaker.player.ui.state

import dom.dima.practicum.playlistmaker.media.domain.models.Playlist

sealed class AudioPlayerState() {

    class Prepared (val data: StateData) : AudioPlayerState()
    class Completion (val data: StateData) : AudioPlayerState()
    class Playing (val data: StateData, val progress: String) : AudioPlayerState()
    class Pause (val data: StateData) : AudioPlayerState()
    class Favorite() : AudioPlayerState()
    class NotFavorite() : AudioPlayerState()
    class Playlists(val playlists : List<Playlist>) : AudioPlayerState()
    class CompleteAddToPlaylist(val title: String) : AudioPlayerState()
    class AlreadyExists(val title: String) : AudioPlayerState()

}