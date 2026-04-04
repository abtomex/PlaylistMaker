package dom.dima.practicum.playlistmaker.player.ui.state

import dom.dima.practicum.playlistmaker.media.domain.models.Playlist

sealed class AudioPlayerState() {

    interface HasData {
        val data: StateData
    }

    class Default (override val data: StateData) : AudioPlayerState(), HasData
    class Prepared (override val data: StateData) : AudioPlayerState(), HasData
    class Completion (override val data: StateData) : AudioPlayerState(), HasData
    class Playing (override val data: StateData, val progress: String) : AudioPlayerState(), HasData
    class Pause (override val data: StateData) : AudioPlayerState(), HasData
    class Favorite() : AudioPlayerState()
    class NotFavorite() : AudioPlayerState()
    class Playlists(val playlists : List<Playlist>) : AudioPlayerState()
    class CompleteAddToPlaylist(val title: String) : AudioPlayerState()
    class AlreadyExists(val title: String) : AudioPlayerState()

}